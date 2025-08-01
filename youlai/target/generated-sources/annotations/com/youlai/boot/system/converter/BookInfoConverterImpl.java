package com.youlai.boot.system.converter;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-21T10:41:50+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
@Component
public class BookInfoConverterImpl implements BookInfoConverter {

    @Override
    public BookInfoForm toForm(BookInfo entity) {
        if ( entity == null ) {
            return null;
        }

        BookInfoForm bookInfoForm = new BookInfoForm();

        bookInfoForm.setId( entity.getId() );
        bookInfoForm.setNumber( entity.getNumber() );
        bookInfoForm.setName( entity.getName() );
        bookInfoForm.setPubDate( entity.getPubDate() );
        bookInfoForm.setPrice( entity.getPrice() );
        bookInfoForm.setCreateBy( entity.getCreateBy() );
        bookInfoForm.setCreateTime( entity.getCreateTime() );
        bookInfoForm.setUpdateBy( entity.getUpdateBy() );
        bookInfoForm.setUpdateTime( entity.getUpdateTime() );
        bookInfoForm.setIsDeleted( entity.getIsDeleted() );

        return bookInfoForm;
    }

    @Override
    public BookInfo toEntity(BookInfoForm formData) {
        if ( formData == null ) {
            return null;
        }

        BookInfo bookInfo = new BookInfo();

        bookInfo.setId( formData.getId() );
        bookInfo.setCreateTime( formData.getCreateTime() );
        bookInfo.setUpdateTime( formData.getUpdateTime() );
        bookInfo.setNumber( formData.getNumber() );
        bookInfo.setName( formData.getName() );
        bookInfo.setPubDate( formData.getPubDate() );
        bookInfo.setPrice( formData.getPrice() );
        bookInfo.setCreateBy( formData.getCreateBy() );
        bookInfo.setUpdateBy( formData.getUpdateBy() );
        bookInfo.setIsDeleted( formData.getIsDeleted() );

        return bookInfo;
    }
}
