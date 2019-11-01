# 参数注入
-keepclassmembers class * {
    @com.zanfou.polaris.annotation.field.FieldInject <fields>;
}
# keep annotated by polaris.keep
-keep @com.zanfou.polaris.annotation.keep.Keep class * {*;}
-keepclassmembers class * {
    @com.zanfou.polaris.annotation.keep.Keep <fields>;
}
-keepclassmembers class * {
    @com.zanfou.polaris.annotation.keep.Keep <methods>;
}