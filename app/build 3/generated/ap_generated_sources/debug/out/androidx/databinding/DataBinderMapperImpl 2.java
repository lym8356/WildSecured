package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.fit5046.wildsecured.DataBinderMapperImpl());
  }
}
