package com.planb.sniffer.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JsonUtils {
    private static final String NULL = "null";

    public static class JsonStringBuilder {
        public enum Step {
            TWO_SPACES(2), THREE_SPACES(3), FOUR_SPACES(4), COMPACT(0), TABS(1);
            private final int ident;

            Step(int ident) {
                this.ident = ident;
            }

            public int getIdent() {
                return ident;
            }
        }

        public enum Type {
            PURE("", "\n", "", "\""), JAVA("\"", "\\n\"\n + \"", "\";", "\\\"");
            private final String initial;
            private final String newLine;
            private final String tailLine;
            private final String wrapLine;

            Type(String initial, String newLine, String tailLine, String wrapLine) {
                this.initial = initial;
                this.newLine = newLine;
                this.tailLine = tailLine;
                this.wrapLine = wrapLine;
            }

            public String getInitial() {
                return initial;
            }

            public String getNewLine() {
                return newLine;
            }

            public String getTailLine() {
                return tailLine;
            }

            public String getWrapLine() {
                return wrapLine;
            }
        }

        private final StringBuilder builder;
        private final Step identStep;
        private final Type type;
        private int ident;

        public JsonStringBuilder(Step identStep) {
            builder = new StringBuilder(Type.PURE.getInitial());
            this.identStep = identStep;
            this.type = Type.PURE;
        }

        public JsonStringBuilder(Type type) {
            builder = new StringBuilder(type.getInitial());
            this.identStep = Step.TWO_SPACES;
            this.type = type;
        }

        public JsonStringBuilder() {
            builder = new StringBuilder();
            this.identStep = Step.TWO_SPACES;
            this.type = Type.PURE;
        }

        public JsonStringBuilder append(final char character) {
            builder.append(character);
            return this;
        }

        public JsonStringBuilder append(final String string) {
            builder.append(string);
            return this;
        }

        public JsonStringBuilder fillSpaces() {
            for (int index = 0; index < ident; index += 1) {
                builder.append(identStep == Step.TABS ? '\t' : ' ');
            }
            return this;
        }

        public JsonStringBuilder incIdent() {
            ident += identStep.getIdent();
            return this;
        }

        public JsonStringBuilder decIdent() {
            ident -= identStep.getIdent();
            return this;
        }

        public JsonStringBuilder newLine() {
            if (identStep != Step.COMPACT) {
                builder.append(type.getNewLine());
            }
            return this;
        }

        public Step getIdentStep() {
            return identStep;
        }

        @Override
        @NotNull
        public String toString() {
            return builder.toString() + type.getTailLine();
        }
    }

    public static class JsonArray {
        public static void writeJson(Collection collection, JsonStringBuilder builder) {
            if (collection == null) {
                builder.append(NULL);
                return;
            }

            Iterator iter = collection.iterator();

            builder.append('[').incIdent();
            if (!collection.isEmpty()) {
                builder.newLine();
            }
            while (iter.hasNext()) {
                Object value = iter.next();
                builder.fillSpaces();
                JsonValue.writeJson(value, builder);
                if (iter.hasNext()) {
                    builder.append(',').newLine();
                }
            }
            builder.newLine().decIdent().fillSpaces().append(']');
        }

        public static void writeJson(byte[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(short[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(int[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(long[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(float[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(double[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(boolean[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append(String.valueOf(array[0]));

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(array[i]));
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(char[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIdent().newLine();
                builder.fillSpaces().append('\"').append(String.valueOf(array[0])).append('\"');

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append('\"').append(String.valueOf(array[i])).append('\"');
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }

        public static void writeJson(Object[] array, JsonStringBuilder builder) {
            if (array == null) {
                builder.append(NULL);
            } else if (array.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').newLine().incIdent().fillSpaces();
                JsonValue.writeJson(array[0], builder);

                for (int i = 1; i < array.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    JsonValue.writeJson(array[i], builder);
                }

                builder.newLine().decIdent().fillSpaces().append(']');
            }
        }
    }

    public static class JsonObject {
        public static void writeJson(Map map, JsonStringBuilder builder) {
            if (map == null) {
                builder.append(NULL);
                return;
            }

            Iterator iter = map.entrySet().iterator();

            builder.append('{').incIdent();
            if (!map.isEmpty()) {
                builder.newLine();
            }
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                builder.fillSpaces().append(builder.type.getWrapLine());
                builder.append(JsonValue.escape(String.valueOf(entry.getKey())));
                builder.append(builder.type.getWrapLine());
                builder.append(':');
                if (builder.getIdentStep() != JsonStringBuilder.Step.COMPACT) {
                    builder.append(' ');
                }
                JsonValue.writeJson(entry.getValue(), builder);
                if (iter.hasNext()) {
                    builder.append(',').newLine();
                }
            }
            builder.newLine().decIdent().fillSpaces().append('}');
        }
    }

    public static class JsonValue {
        public static void writeJson(Object value, JsonStringBuilder builder) {
            if (value == null) {
                builder.append(NULL);
            } else if (value instanceof String) {
                builder.append(builder.type.getWrapLine())
                        .append(escape((String) value)).append(builder.type.getWrapLine());
            } else if (value instanceof Double) {
                if (((Double) value).isInfinite() || ((Double) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Float) {
                if (((Float) value).isInfinite() || ((Float) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Number) {
                builder.append(value.toString());
            } else if (value instanceof Boolean) {
                builder.append(value.toString());
            } else if (value instanceof Map) {
                JsonObject.writeJson((Map) value, builder);
            } else if (value instanceof Collection) {
                JsonArray.writeJson((Collection) value, builder);
            } else if (value instanceof byte[]) {
                JsonArray.writeJson((byte[]) value, builder);
            } else if (value instanceof short[]) {
                JsonArray.writeJson((short[]) value, builder);
            } else if (value instanceof int[]) {
                JsonArray.writeJson((int[]) value, builder);
            } else if (value instanceof long[]) {
                JsonArray.writeJson((long[]) value, builder);
            } else if (value instanceof float[]) {
                JsonArray.writeJson((float[]) value, builder);
            } else if (value instanceof double[]) {
                JsonArray.writeJson((double[]) value, builder);
            } else if (value instanceof boolean[]) {
                JsonArray.writeJson((boolean[]) value, builder);
            } else if (value instanceof char[]) {
                JsonArray.writeJson((char[]) value, builder);
            } else if (value instanceof Object[]) {
                JsonArray.writeJson((Object[]) value, builder);
            } else {
                builder.append(value.toString());
            }
        }

        public static String escape(String s) {
            if (s == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            escape(s, sb);
            return sb.toString();
        }

        private static void escape(String s, StringBuilder sb) {
            final int len = s.length();
            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);
                switch (ch) {
                    case '"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '€':
                        sb.append("€");
                        break;
                    default:
                        if (ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F'
                                || ch >= '\u2000' && ch <= '\u20FF') {
                            String ss = Integer.toHexString(ch);
                            sb.append("\\u");
                            for (int k = 0; k < 4 - ss.length(); k++) {
                                sb.append('0');
                            }
                            sb.append(ss.toUpperCase());
                        } else {
                            sb.append(ch);
                        }
                        break;
                }
            }
        }
    }

    public static class ParseException extends RuntimeException {
        private final int offset;
        private final int line;
        private final int column;

        public ParseException(String message, int offset, int line, int column) {
            super(message + " at " + line + ":" + column);
            this.offset = offset;
            this.line = line;
            this.column = column;
        }

        public int getOffset() {
            return offset;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }
    }

    public static class JsonParser {
        private final String json;
        private int index;
        private int line;
        private int lineOffset;
        private int current;
        private StringBuilder captureBuffer;
        private int captureStart;

        public JsonParser(String string) {
            this.json = string;
            line = 1;
            captureStart = -1;
        }

        public Object parse() {
            read();
            skipWhiteSpace();
            final Object result = readValue();
            skipWhiteSpace();
            if (!isEndOfText()) {
                throw error("Unexpected character");
            }
            return result;
        }

        private Object readValue() {
            switch (current) {
                case 'n':
                    return readNull();
                case 't':
                    return readTrue();
                case 'f':
                    return readFalse();
                case '"':
                    return readString();
                case '[':
                    return readArray();
                case '{':
                    return readObject();
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return readNumber();
                default:
                    throw expected("value");
            }
        }

        private List<Object> readArray() {
            read();
            List<Object> array = new ArrayList<>();
            skipWhiteSpace();
            if (readChar(']')) {
                return array;
            }
            do {
                skipWhiteSpace();
                array.add(readValue());
                skipWhiteSpace();
            } while (readChar(','));
            if (!readChar(']')) {
                throw expected("',' or ']'");
            }
            return array;
        }

        private Map<String, Object> readObject() {
            read();
            Map<String, Object> object = new LinkedHashMap<>();
            skipWhiteSpace();
            if (readChar('}')) {
                return object;
            }
            do {
                skipWhiteSpace();
                String name = readName();
                skipWhiteSpace();
                if (!readChar(':')) {
                    throw expected("':'");
                }
                skipWhiteSpace();
                object.put(name, readValue());
                skipWhiteSpace();
            } while (readChar(','));
            if (!readChar('}')) {
                throw expected("',' or '}'");
            }
            return object;
        }

        private String readName() {
            if (current != '"') {
                throw expected("name");
            }
            return readString();
        }

        private String readNull() {
            read();
            readRequiredChar('u');
            readRequiredChar('l');
            readRequiredChar('l');
            return null;
        }

        private Boolean readTrue() {
            read();
            readRequiredChar('r');
            readRequiredChar('u');
            readRequiredChar('e');
            return Boolean.TRUE;
        }

        private Boolean readFalse() {
            read();
            readRequiredChar('a');
            readRequiredChar('l');
            readRequiredChar('s');
            readRequiredChar('e');
            return Boolean.FALSE;
        }

        private void readRequiredChar(char ch) {
            if (!readChar(ch)) {
                throw expected("'" + ch + "'");
            }
        }

        private String readString() {
            read();
            startCapture();
            while (current != '"') {
                if (current == '\\') {
                    pauseCapture();
                    readEscape();
                    startCapture();
                } else if (current < 0x20) {
                    throw expected("valid string character");
                } else {
                    read();
                }
            }
            String string = endCapture();
            read();
            return string;
        }

        private void readEscape() {
            read();
            switch (current) {
                case '"':
                case '/':
                case '\\':
                    captureBuffer.append((char) current);
                    break;
                case 'b':
                    captureBuffer.append('\b');
                    break;
                case 'f':
                    captureBuffer.append('\f');
                    break;
                case 'n':
                    captureBuffer.append('\n');
                    break;
                case 'r':
                    captureBuffer.append('\r');
                    break;
                case 't':
                    captureBuffer.append('\t');
                    break;
                case 'u':
                    char[] hexChars = new char[4];
                    boolean isHexCharsDigits = true;
                    for (int i = 0; i < 4; i++) {
                        read();
                        if (!isHexDigit()) {
                            isHexCharsDigits = false;
                        }
                        hexChars[i] = (char) current;
                    }
                    if (isHexCharsDigits) {
                        captureBuffer.append((char) Integer.parseInt(new String(hexChars), 16));
                    } else {
                        captureBuffer.append("\\u").append(hexChars[0]).append(hexChars[1]).append(hexChars[2])
                                .append(hexChars[3]);
                    }
                    break;
                default:
                    throw expected("valid escape sequence");
            }
            read();
        }

        private Number readNumber() {
            startCapture();
            readChar('-');
            int firstDigit = current;
            if (!readDigit()) {
                throw expected("digit");
            }
            if (firstDigit != '0') {
                while (readDigit()) {
                }
            }
            readFraction();
            readExponent();
            final String number = endCapture();
            final Number result;
            if (number.contains(".") || number.contains("e") || number.contains("E")) {
                if (number.length() > 9) {
                    result = new java.math.BigDecimal(number);
                } else {
                    result = Double.valueOf(number);
                }
            } else {
                if (number.length() > 19) {
                    result = new java.math.BigInteger(number);
                } else {
                    result = Long.valueOf(number);
                }
            }
            return result;
        }

        private boolean readFraction() {
            if (!readChar('.')) {
                return false;
            }
            if (!readDigit()) {
                throw expected("digit");
            }
            while (readDigit()) {
            }
            return true;
        }

        private boolean readExponent() {
            if (!readChar('e') && !readChar('E')) {
                return false;
            }
            if (!readChar('+')) {
                readChar('-');
            }
            if (!readDigit()) {
                throw expected("digit");
            }
            while (readDigit()) {
            }
            return true;
        }

        private boolean readChar(char ch) {
            if (current != ch) {
                return false;
            }
            read();
            return true;
        }

        private boolean readDigit() {
            if (!isDigit()) {
                return false;
            }
            read();
            return true;
        }

        private void skipWhiteSpace() {
            while (isWhiteSpace()) {
                read();
            }
        }

        private void read() {
            if (index == json.length()) {
                current = -1;
                return;
            }
            if (current == '\n') {
                line++;
                lineOffset = index;
            }
            current = json.charAt(index++);
        }

        private void startCapture() {
            if (captureBuffer == null) {
                captureBuffer = new StringBuilder();
            }
            captureStart = index - 1;
        }

        private void pauseCapture() {
            captureBuffer.append(json, captureStart, index - 1);
            captureStart = -1;
        }

        private String endCapture() {
            int end = current == -1 ? index : index - 1;
            String captured;
            if (captureBuffer.length() > 0) {
                captureBuffer.append(json, captureStart, end);
                captured = captureBuffer.toString();
                captureBuffer.setLength(0);
            } else {
                captured = json.substring(captureStart, end);
            }
            captureStart = -1;
            return captured;
        }

        private ParseException expected(String expected) {
            if (isEndOfText()) {
                return error("Unexpected end of input");
            }
            return error("Expected " + expected);
        }

        private ParseException error(String message) {
            int absIndex = index;
            int column = absIndex - lineOffset;
            int offset = isEndOfText() ? absIndex : absIndex - 1;
            return new ParseException(message, offset, line, column - 1);
        }

        private boolean isWhiteSpace() {
            return current == ' ' || current == '\t' || current == '\n' || current == '\r';
        }

        private boolean isDigit() {
            return current >= '0' && current <= '9';
        }

        private boolean isHexDigit() {
            return isDigit() || current >= 'a' && current <= 'f' || current >= 'A' && current <= 'F';
        }

        private boolean isEndOfText() {
            return current == -1;
        }

    }

    public static String toJson(Collection collection, JsonStringBuilder.Step identStep) {
        final JsonStringBuilder builder = new JsonStringBuilder(identStep);

        JsonArray.writeJson(collection, builder);
        return builder.toString();
    }

    public static String toJson(Collection collection) {
        return toJson(collection, JsonStringBuilder.Step.TWO_SPACES);
    }

    public static String toJson(Map map, JsonStringBuilder.Step identStep) {
        final JsonStringBuilder builder = new JsonStringBuilder(identStep);

        JsonObject.writeJson(map, builder);
        return builder.toString();
    }

    public static String toJson(Map map) {
        return toJson(map, JsonStringBuilder.Step.TWO_SPACES);
    }

    public static String toJsonJavaString(Collection collection) {
        final JsonStringBuilder builder = new JsonStringBuilder(JsonStringBuilder.Type.JAVA);

        JsonArray.writeJson(collection, builder);
        return builder.toString();
    }

    public static String toJsonJavaString(Map map) {
        final JsonStringBuilder builder = new JsonStringBuilder(JsonStringBuilder.Type.JAVA);

        JsonObject.writeJson(map, builder);
        return builder.toString();
    }

    public static Object fromJson(String string) {
        return new JsonParser(string).parse();
    }

    public static String formatJson(String json, JsonStringBuilder.Step identStep) {
        Object result = fromJson(json);
        if (result instanceof Map) {
            return toJson((Map) result, identStep);
        }
        return toJson((List) result, identStep);
    }

    public static String formatJson(String json) {
        return formatJson(json, JsonStringBuilder.Step.THREE_SPACES);
    }

    public static String formatJsonSafe(String json) {
        try {
            return formatJson(json);
        } catch (Exception e) {
            e.printStackTrace();
            return "格式化失败:\n" + e.getMessage();
        }
    }

    public static String formatJsonSafe(String json, JsonStringBuilder.Step identStep) {
        try {
            return formatJson(json, identStep);
        } catch (Exception e) {
            e.printStackTrace();
            return "格式化失败:\n" + e.getMessage();
        }
    }

    @TestOnly
    public static void main(String[] args) {
        String a = "{\"success\":false,\"data\":[{\"id\":1,\"content\":\"型不海政保压济己主政它便五半维美应过且图把九研题口完\",\"video\":{\"source\":\"https://uploadbeta.com/api/pictures/random/?key=动漫&317447\",\"cover\":\"http://dummyimage.com/500x500/FF4D32/FFF&text=Spark\",\"width\":\"500\",\"height\":\"700\",\"duration\":30,\"size\":2048},\"shareNum\":71518,\"likeNum\":41503,\"commentNum\":75352,\"product\":{\"id\":1001,\"name\":\"UHWSV!PMpydXIOpxIn8^TQ[%shb[vFj8iaNZJM8%R41dL9a6N\",\"type\":0,\"imageUrl\":\"https://uploadbeta.com/api/pictures/random/?key=动漫&267869\",\"actors\":\"较济市(7, 50)\",\"pubDate\":\"2008-01-16\",\"wishNum\":49506,\"score\":49},\"topic\":{\"id\":1002,\"title\":\"市走消育\"},\"geng\":{\"id\":1003,\"title\":\"族信口油做必别及\",\"partakeNum\":9389},\"productPoint\":3071,\"user\":{\"id\":1234,\"nickName\":\"象少化军命应求员领革十金持须况规拉里议九组引了区制世根有少取现是报市只属得装石参新二队子色约界\",\"avatarUrl\":\"https://uploadbeta.com/api/pictures/random/?key=动漫&83012\"},\"followBehavior\":false,\"likeBehavior\":true},{\"id\":1,\"content\":\"动花公候干利量下加较战口么提儿了了自出选产和适道生片调白权民造头是究团象门候重认具权色着教位想接市\",\"video\":{\"source\":\"https://uploadbeta.com/api/pictures/random/?key=动漫&625549\",\"cover\":\"http://dummyimage.com/500x500/FF4D32/FFF&text=Spark\",\"width\":\"100\",\"height\":\"700\",\"duration\":30,\"size\":2048},\"shareNum\":22191,\"likeNum\":58838,\"commentNum\":66042,\"product\":{\"id\":1001,\"name\":\"Ve)eevD^H9AAtd3XK5qZ@nEsS5xgx&unN3x5gxW#t3T\",\"type\":0,\"imageUrl\":\"https://uploadbeta.com/api/pictures/random/?key=动漫&157392\",\"actors\":\"做九工每(7, 50)\",\"pubDate\":\"2008-05-12\",\"wishNum\":46059,\"score\":72},\"topic\":{\"id\":1002,\"title\":\"结无团指称则业方基作美的对达两技识安群太发资亲则交温务片至口好后市至人单真快土快\"},\"geng\":{\"id\":1003,\"title\":\"管才交米约局历建想空信长反金开应式体器中实格务况治最较\",\"partakeNum\":5642},\"productPoint\":3981,\"user\":{\"id\":1234,\"nickName\":\"确参眼山号几低体府回节接当教思养业具发连统实始此制称月\",\"avatarUrl\":\"https://uploadbeta.com/api/pictures/random/?key=动漫&618981\"},\"followBehavior\":false,\"likeBehavior\":false}],\"paging\":{\"offset\":1,\"limit\":15,\"total\":20,\"hasMore\":false,\"timestamp\":1111}}";
        System.out.println(JsonUtils.formatJson(a, JsonUtils.JsonStringBuilder.Step.TWO_SPACES));
    }
}