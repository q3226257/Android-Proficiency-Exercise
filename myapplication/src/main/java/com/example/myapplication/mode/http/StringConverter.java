package com.example.myapplication.mode.http;

public static class StringConverter implements Converter<ResponseBody, String> {

  public static final StringConverter INSTANCE = new StringConverter();

  @Override
  public String convert(ResponseBody value) throws IOException {
    return value.string();
  }
}

作者：怪盗kidou
链接：http://www.jianshu.com/p/308f3c54abdd
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。