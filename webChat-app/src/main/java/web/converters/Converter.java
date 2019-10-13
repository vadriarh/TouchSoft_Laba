package web.converters;


public interface Converter<InternalContext, ExternalContext> {
    ExternalContext convertToExternalContext(InternalContext internal);

    InternalContext convertToInternalContext(ExternalContext external);

}
