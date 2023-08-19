package br.com.wandaymo.consulrest.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import lombok.Setter;

public class LogPattern extends ch.qos.logback.classic.PatternLayout {

    @Setter
    private Pattern multilinePattern;

    @Override
    public void start() {
        List<String> maskPatterns = new ArrayList<>();
        maskPatterns.add("cpfCnpj=\\d{3}.*?(\\d{3}).*?(\\d{3}).*?-?\\d{2},");
        multilinePattern = Pattern.compile(String.join("|", maskPatterns), Pattern.MULTILINE);
        super.start();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        return maskMessage(super.doLayout(event));
    }

    private String maskMessage(String message) {
        if (multilinePattern == null) {
            return message;
        }
        StringBuilder sb = new StringBuilder(message);
        Matcher matcher = multilinePattern.matcher(sb);
        while (matcher.find()) {
            IntStream.rangeClosed(1, matcher.groupCount()).forEach(group -> {
                if (matcher.group(group) != null) {
                    IntStream.range(matcher.start(group), matcher.end(group)).forEach(i -> sb.setCharAt(i, '*'));
                }
            });
        }
        return sb.toString();
    }
}
