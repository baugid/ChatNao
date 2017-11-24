package configs;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Configs {

    public static void loadFromFile(Config config, String filePath) throws IOException {
        String[] settings = Files.lines(Paths.get(filePath)).filter(s -> s.matches("^.+" + config.getKeyValueDivider() + ".+$") && !s.matches("^[\\s\\t]*#.*$")).toArray(String[]::new);
        HashMap<String, Field> nameFieldMap = readConfigObject(config);
        for (String setting : settings) {
            String[] keyVal = removeSpacesAroundSplit(setting, config.getKeyValueDivider()).split(config.getKeyValueDivider(), 2);
            if (nameFieldMap.containsKey(keyVal[0]))
                setConfigSetting(config, nameFieldMap.get(keyVal[0]), keyVal[1]);
        }
    }

    private static HashMap<String, Field> readConfigObject(Config config) {
        HashMap<String, Field> map = new HashMap<>();
        for (Field f : config.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(Private.class)) {
                String name;
                if (f.isAnnotationPresent(Rename.class))
                    name = f.getAnnotation(Rename.class).value();
                else
                    name = f.getName();
                assert !map.containsKey(name);
                f.setAccessible(true);
                map.put(name, f);
            }
        }
        return map;
    }

    private static void setConfigSetting(Config config, Field setting, String value) {
        try {
            Class<?> type = setting.getType();
            if (type.isPrimitive()) {
                if (type == int.class)
                    setting.setInt(config, Integer.parseInt(value));
                else if (type == char.class)
                    setting.setChar(config, value.charAt(0));
                else if (type == short.class)
                    setting.setShort(config, Short.parseShort(value));
                else if (type == long.class)
                    setting.setLong(config, Long.parseLong(value));
                else if (type == byte.class)
                    setting.setByte(config, Byte.parseByte(value));
                else if (type == float.class)
                    setting.setFloat(config, Float.parseFloat(value));
                else if (type == double.class)
                    setting.setDouble(config, Double.parseDouble(value));
                else if (type == boolean.class)
                    setting.setBoolean(config, Boolean.parseBoolean(value));
            } else if (ConfigObject.class.isAssignableFrom(type))
                ((ConfigObject) setting.get(config)).parseString(value);
            else if (type == String.class)
                setting.set(config, value);
            else if (hasStringConstructor(type))
                setting.set(config, type.getConstructor(String.class).newInstance(value));
        } catch (Exception ignored) {
        }
    }

    private static boolean hasStringConstructor(Class<?> type) {
        try {
            return type.getConstructor(String.class).isAccessible();
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static String removeSpacesAroundSplit(String s, String splitString) {
        StringBuilder b = new StringBuilder(s);
        for (int i = b.indexOf(splitString); i >= 0; i--) {
            if (b.charAt(i) == ' ')
                b.deleteCharAt(i);
            else
                break;
        }
        for (int i = b.indexOf(splitString); i < b.length(); i++) {
            if (b.charAt(i) == ' ')
                b.deleteCharAt(i);
            else
                break;
        }
        return b.toString();
    }
}
