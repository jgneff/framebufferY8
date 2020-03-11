{% if include.name contains "jdk" %}[Log File](https://github.com/jgneff/framebufferY8/blob/master/logs/{{ include.name }}.log) • {% endif %}
[PNG Chart]({{ include.name }}.png) •
[SVG Chart]({{ include.name }}.svg)

![Bar Chart]({{ include.name }}.svg){:width="{{ include.width }}" height="{{ include.height }}"}

{% include {{ include.name | replace: "+", "_" }}.md %}
