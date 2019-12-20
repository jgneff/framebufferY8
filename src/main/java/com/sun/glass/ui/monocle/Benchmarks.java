/*
 * Copyright (C) 2019 John Neffenger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sun.glass.ui.monocle;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.BenchmarkParams;

/**
 * This class compares the output and performance of the methods in the new
 * {@link FramebufferY8} class with those in the old {@link Framebuffer} class.
 * The tests invoke the methods that convert and copy a 32-bit source image in
 * ARGB32 pixel format into a target image in one of three pixel formats: 8-bit
 * Y8 grayscale, 16-bit RGB565 color, or 32-bit ARGB32 color.
 * <p>
 * When the benchmark mode is {@link Mode#SingleShotTime}, this class saves the
 * content of the source buffer and all target buffers and channels as files in
 * the PNG image format. Run the tests in this mode with the command:</p>
 * <pre>{@code
 * $ java -jar target/benchmarks.jar -f 1 -bm ss
 * }</pre>
 * <p>
 * The source image depends on the value of the <i>type</i> benchmark parameter
 * as follows:</p>
 * <dl>
 * <dt>color</dt>
 * <dd>The source image is a fully opaque 24-bit color square of 4,096 × 4,096
 * pixels with all values of 8-bit red, green, and blue. Use this value to
 * verify that the methods correctly convert all color values.</dd>
 * <dt>alpha</dt>
 * <dd>The source image is a 16-bit color square of 256 × 256 pixels with all
 * values of 8-bit alpha and red. Use this value to verify that the methods
 * correctly convert all alpha values.</dd>
 * <dt>small</dt>
 * <dd>The source image is a color rectangle of
 * {@value SMALL_WIDTH} × {@value SMALL_HEIGHT} pixels. Use this value to
 * measure the frames per second on older ARM devices with 6-inch displays at
 * 167 pixels per inch.</dd>
 * <dt>large</dt>
 * <dd>The source image is a color rectangle of
 * {@value LARGE_WIDTH} × {@value LARGE_HEIGHT} pixels. Use this value to
 * measure the frames per second on newer ARM devices with 6-inch displays at
 * 300 pixels per inch.</dd>
 * </dl>
 * <p>
 * The default value of the <i>type</i> parameter is <i>color</i>. Change the
 * <i>type</i> to <i>large</i>, for example, with the command:</p>
 * <pre>{@code
 * $ java -jar target/benchmarks.jar -p type=large
 * }</pre>
 *
 * @author John Neffenger
 */
@State(Scope.Benchmark)
public class Benchmarks {

    private static final String FORMAT = "png";
    private static final String SUFFIX = ".png";

    private static final String ARGB32_SOURCE = "argb32";

    private static final String NEW_BYTE_BUFFER = "buffer08new";
    private static final String NEW_SHORT_BUFFER = "buffer16new";
    private static final String OLD_SHORT_BUFFER = "buffer16old";
    private static final String NEW_INT_BUFFER = "buffer32new";
    private static final String OLD_INT_BUFFER = "buffer32old";

    private static final String NEW_BYTE_CHANNEL = "output08new";
    private static final String NEW_SHORT_CHANNEL = "output16new";
    private static final String OLD_SHORT_CHANNEL = "output16old";
    private static final String NEW_INT_CHANNEL = "output32new";
    private static final String OLD_INT_CHANNEL = "output32old";

    /**
     * The width in pixels of older 6-inch E Ink Pearl displays at 167 ppi.
     */
    private static final int SMALL_WIDTH = 800;

    /**
     * The height in pixels of older 6-inch E Ink Pearl displays at 167 ppi.
     */
    private static final int SMALL_HEIGHT = 600;

    /**
     * The width in pixels of newer 6-inch E Ink Carta displays at 300 ppi.
     */
    private static final int LARGE_WIDTH = 1448;

    /**
     * The height in pixels of newer 6-inch E Ink Carta displays at 300 ppi.
     */
    private static final int LARGE_HEIGHT = 1072;

    /**
     * The number of pixels on each side of an opaque color square with all
     * values of 8-bit red, green, and blue, which is √(2^24) = 2^12 = 4096.
     */
    private static final int RGB24_SIDE = 4096;

    /**
     * The number of pixels on each side of a color square with all values of
     * 8-bit alpha and red, which is √(2^16) = 2^8 = 256.
     */
    private static final int AR16_SIDE = 256;

    private static int width;
    private static int height;

    /**
     * Indicates which frame buffer source created the target output:
     * {@code true} if the source is the new {@link FramebufferY8} class;
     * otherwise {@code false}, meaning the source is the old
     * {@link Framebuffer} class. This field is used to choose the file names
     * for the saved target images.
     */
    private static volatile boolean sourceIsNew;

    /**
     * Indicates whether to save the source buffer and all target buffers and
     * channels as image files.
     */
    private static volatile boolean saveImages;

    /**
     * Coordinates saving the source image among multiple worker threads.
     */
    private static AtomicBoolean firstSourceToFinish;

    /**
     * Coordinates saving the target image among multiple worker threads.
     */
    private static AtomicBoolean firstTargetToFinish;

    private static void write(BufferedImage image, File file) {
        try {
            ImageIO.write(image, FORMAT, file);
        } catch (IOException e) {
            System.err.println(String.format("Error saving %s (%s)", file, e));
        }
    }

    private static void save(ByteBuffer pixels, File file) {
        var image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        pixels.get(data);
        write(image, file);
    }

    private static void save(ShortBuffer pixels, File file) {
        var image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
        short[] data = ((DataBufferUShort) image.getRaster().getDataBuffer()).getData();
        pixels.get(data);
        write(image, file);
    }

    private static void save(IntBuffer pixels, File file) {
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        pixels.get(data);
        write(image, file);
    }

    /**
     * Represents one of four image types:
     * <dl>
     * <dt>color</dt>
     * <dd>an opaque image with all 24-bit RGB color values</dd>
     * <dt>alpha</dt>
     * <dd>an image with all red and transparency values</dd>
     * <dt>small</dt>
     * <dd>a color image of {@value SMALL_WIDTH} × {@value SMALL_HEIGHT}
     * pixels</dd>
     * <dt>large</dt>
     * <dd>a color image of {@value LARGE_WIDTH} × {@value LARGE_HEIGHT}
     * pixels</dd>
     * </dl>
     */
    public enum ImageType {
        color(RGB24_SIDE, RGB24_SIDE),
        alpha(AR16_SIDE, AR16_SIDE),
        small(SMALL_WIDTH, SMALL_HEIGHT),
        large(LARGE_WIDTH, LARGE_HEIGHT);

        private final int width;
        private final int height;

        ImageType(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Creates the off-screen composition buffer containing one of several types
     * of source images, depending on the value of the benchmark parameter
     * {@code type}. The default value of {@code type} is {@code color}.
     */
    @State(Scope.Thread)
    public static class CompositionBuffer {

        private static final int SHIFT_ALPHA = 16;
        private static final int MAKE_OPAQUE = 0xFF000000;

        @Param("color")
        public ImageType type;

        private ByteBuffer bb;
        private IntBuffer pixels;

        private IntConsumer getAction(ImageType type) {
            switch (type) {
                case alpha:
                    return i -> pixels.put(i << SHIFT_ALPHA);
                default:
                    return i -> pixels.put(i | MAKE_OPAQUE);
            }
        }

        @Setup
        public void setup() {
            width = type.width;
            height = type.height;
            IntConsumer action = getAction(type);
            /*
             * The JavaFX code in EPDFrameBuffer.getOffscreenBuffer allocates a
             * direct byte buffer for the off-screen composition buffer.
             */
            bb = ByteBuffer.allocateDirect(width * height * Integer.BYTES);
            bb.order(ByteOrder.nativeOrder());
            pixels = bb.asIntBuffer();
            IntStream.range(0, width * height).forEachOrdered(action);
            pixels.flip();
        }

        @TearDown
        public void tearDown() {
            if (saveImages && firstSourceToFinish.compareAndSet(false, true)) {
                var file = new File(ARGB32_SOURCE + SUFFIX);
                if (!file.exists()) {
                    save(pixels, file);
                }
            }
        }
    }

    /**
     * Creates the {@link FramebufferY8} source for an 8-bit target.
     */
    @State(Scope.Thread)
    public static class NewByteSource {

        private Framebuffer fb;

        @Setup
        public void setup(CompositionBuffer buffer) {
            fb = new FramebufferY8(buffer.bb, width, height, Byte.SIZE, true);
            sourceIsNew = true;
        }
    }

    /**
     * Creates the {@link FramebufferY8} source for a 16-bit target.
     */
    @State(Scope.Thread)
    public static class NewShortSource {

        private Framebuffer fb;

        @Setup
        public void setup(CompositionBuffer buffer) {
            fb = new FramebufferY8(buffer.bb, width, height, Short.SIZE, true);
            sourceIsNew = true;
        }
    }

    /**
     * Creates the {@link FramebufferY8} source for a 32-bit target.
     */
    @State(Scope.Thread)
    public static class NewIntSource {

        private Framebuffer fb;

        @Setup
        public void setup(CompositionBuffer buffer) {
            fb = new FramebufferY8(buffer.bb, width, height, Integer.SIZE, true);
            sourceIsNew = true;
        }
    }

    /**
     * Creates the {@link Framebuffer} source for a 16-bit target.
     */
    @State(Scope.Thread)
    public static class OldShortSource {

        private Framebuffer fb;

        @Setup
        public void setup(CompositionBuffer buffer) {
            fb = new Framebuffer(buffer.bb, width, height, Short.SIZE, true);
            sourceIsNew = false;
        }
    }

    /**
     * Creates the {@link Framebuffer} source for a 32-bit target.
     */
    @State(Scope.Thread)
    public static class OldIntSource {

        private Framebuffer fb;

        @Setup
        public void setup(CompositionBuffer buffer) {
            fb = new Framebuffer(buffer.bb, width, height, Integer.SIZE, true);
            sourceIsNew = false;
        }
    }

    /**
     * Creates the {@code ByteBuffer} 8-bit target. Allocates the byte buffer on
     * the heap like the {@code WritableByteChannel} output stream.
     */
    @State(Scope.Thread)
    public static class ByteBufferTarget {

        private ByteBuffer buffer;

        @Setup
        public void setup() {
            buffer = ByteBuffer.allocate(width * height * Byte.BYTES);
            buffer.order(ByteOrder.nativeOrder());
        }

        @TearDown
        public void tearDown() {
            if (saveImages && firstTargetToFinish.compareAndSet(false, true)) {
                var file = new File(NEW_BYTE_BUFFER + SUFFIX);
                if (!file.exists()) {
                    save(buffer, file);
                }
            }
        }
    }

    /**
     * Creates the {@code ByteBuffer} 16-bit target. Allocates the byte buffer
     * on the heap like the {@code WritableByteChannel} output stream.
     */
    @State(Scope.Thread)
    public static class ShortBufferTarget {

        private ByteBuffer buffer;

        @Setup
        public void setup() {
            buffer = ByteBuffer.allocate(width * height * Short.BYTES);
            buffer.order(ByteOrder.nativeOrder());
        }

        @TearDown
        public void tearDown() {
            if (saveImages && firstTargetToFinish.compareAndSet(false, true)) {
                var name = sourceIsNew ? NEW_SHORT_BUFFER : OLD_SHORT_BUFFER;
                var file = new File(name + SUFFIX);
                if (!file.exists()) {
                    save(buffer.asShortBuffer(), file);
                }
            }
        }
    }

    /**
     * Creates the {@code ByteBuffer} 32-bit target. Allocates the byte buffer
     * on the heap like the {@code WritableByteChannel} output stream.
     */
    @State(Scope.Thread)
    public static class IntBufferTarget {

        private ByteBuffer buffer;

        @Setup
        public void setup() {
            buffer = ByteBuffer.allocate(width * height * Integer.BYTES);
            buffer.order(ByteOrder.nativeOrder());
        }

        @TearDown
        public void tearDown() {
            if (saveImages && firstTargetToFinish.compareAndSet(false, true)) {
                var name = sourceIsNew ? NEW_INT_BUFFER : OLD_INT_BUFFER;
                var file = new File(name + SUFFIX);
                if (!file.exists()) {
                    save(buffer.asIntBuffer(), file);
                }
            }
        }
    }

    /**
     * Creates the {@code WritableByteChannel} 8-bit target.
     */
    @State(Scope.Thread)
    public static class ByteChannelTarget {

        private ByteArrayOutputStream output;
        private WritableByteChannel channel;

        @Setup
        public void setup() {
            output = new ByteArrayOutputStream(width * height * Byte.BYTES);
            channel = Channels.newChannel(output);
        }

        @TearDown
        public void tearDown() {
            if (saveImages && firstTargetToFinish.compareAndSet(false, true)) {
                var file = new File(NEW_BYTE_CHANNEL + SUFFIX);
                if (!file.exists()) {
                    var buffer = ByteBuffer.wrap(output.toByteArray());
                    buffer.order(ByteOrder.nativeOrder());
                    save(buffer, file);
                }
            }
        }
    }

    /**
     * Creates the {@code WritableByteChannel} 16-bit target.
     */
    @State(Scope.Thread)
    public static class ShortChannelTarget {

        private ByteArrayOutputStream output;
        private WritableByteChannel channel;

        @Setup
        public void setup() {
            output = new ByteArrayOutputStream(width * height * Short.BYTES);
            channel = Channels.newChannel(output);
        }

        @TearDown
        public void tearDown() {
            if (saveImages && firstTargetToFinish.compareAndSet(false, true)) {
                var name = sourceIsNew ? NEW_SHORT_CHANNEL : OLD_SHORT_CHANNEL;
                var file = new File(name + SUFFIX);
                if (!file.exists()) {
                    var buffer = ByteBuffer.wrap(output.toByteArray());
                    buffer.order(ByteOrder.nativeOrder());
                    save(buffer.asShortBuffer(), file);
                }
            }
        }
    }

    /**
     * Creates the {@code WritableByteChannel} 32-bit target.
     */
    @State(Scope.Thread)
    public static class IntChannelTarget {

        private ByteArrayOutputStream output;
        private WritableByteChannel channel;

        @Setup
        public void setup() {
            output = new ByteArrayOutputStream(width * height * Integer.BYTES);
            channel = Channels.newChannel(output);
        }

        @TearDown
        public void tearDown() {
            if (saveImages && firstTargetToFinish.compareAndSet(false, true)) {
                var name = sourceIsNew ? NEW_INT_CHANNEL : OLD_INT_CHANNEL;
                var file = new File(name + SUFFIX);
                if (!file.exists()) {
                    var buffer = ByteBuffer.wrap(output.toByteArray());
                    buffer.order(ByteOrder.nativeOrder());
                    save(buffer.asIntBuffer(), file);
                }
            }
        }
    }

    @Setup
    public void setup(BenchmarkParams params) {
        saveImages = params.getMode() == Mode.SingleShotTime;
        firstSourceToFinish = new AtomicBoolean();
        firstTargetToFinish = new AtomicBoolean();
    }

    @Benchmark
    public void copyTo08New(NewByteSource source, ByteBufferTarget target) {
        source.fb.copyToBuffer(target.buffer);
        target.buffer.flip();
    }

    @Benchmark
    public void copyTo16New(NewShortSource source, ShortBufferTarget target) {
        source.fb.copyToBuffer(target.buffer);
        target.buffer.flip();
    }

    @Benchmark
    public void copyTo16Old(OldShortSource source, ShortBufferTarget target) {
        source.fb.copyToBuffer(target.buffer);
        target.buffer.flip();
    }

    @Benchmark
    public void copyTo32New(NewIntSource source, IntBufferTarget target) {
        source.fb.copyToBuffer(target.buffer);
        target.buffer.flip();
    }

    @Benchmark
    public void copyTo32Old(OldIntSource source, IntBufferTarget target) {
        source.fb.copyToBuffer(target.buffer);
        target.buffer.flip();
    }

    @Benchmark
    public void writeTo08New(NewByteSource source, ByteChannelTarget target)
            throws IOException {
        target.output.reset();
        source.fb.write(target.channel);
    }

    @Benchmark
    public void writeTo16New(NewShortSource source, ShortChannelTarget target)
            throws IOException {
        target.output.reset();
        source.fb.write(target.channel);
    }

    @Benchmark
    public void writeTo16Old(OldShortSource source, ShortChannelTarget target)
            throws IOException {
        target.output.reset();
        source.fb.write(target.channel);
    }

    @Benchmark
    public void writeTo32New(NewIntSource source, IntChannelTarget target)
            throws IOException {
        target.output.reset();
        source.fb.write(target.channel);
    }

    @Benchmark
    public void writeTo32Old(OldIntSource source, IntChannelTarget target)
            throws IOException {
        target.output.reset();
        source.fb.write(target.channel);
    }
}
