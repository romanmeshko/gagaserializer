package io.shooroop.gaga.impl.deserialize.assemblers;

public class PrimitiveAssembler implements ObjectAssembler<String> {

    @Override
    public Object assembleObjectOf(String clazz, String val) {
        if ("boolean".equals(clazz) || "java.lang.Boolean".equals(clazz)) {
            return Boolean.parseBoolean(val);
        } else if ("byte".equals(clazz) || "java.lang.Byte".equals(clazz)) {
            return Byte.parseByte(val);
        } else if ("char".equals(clazz) || "java.lang.Character".equals(clazz)) {
            return val.charAt(0);
        } else if ("short".equals(clazz) || "java.lang.Short".equals(clazz)) {
            return Short.parseShort(val);
        } else if ("int".equals(clazz) || "java.lang.Integer".equals(clazz)) {
            return Integer.parseInt(val);
        } else if ("long".equals(clazz) || "java.lang.Long".equals(clazz)) {
            return Long.parseLong(val);
        } else if ("float".equals(clazz) || "java.lang.Float".equals(clazz)) {
            return Float.parseFloat(val);
        } else if ("double".equals(clazz) || "java.lang.Double".equals(clazz)) {
            return Double.parseDouble(val);
        } else {
            return val;
        }
    }

}
