package com.example.demo.dto;

import com.example.demo.entity.BookFormat;
import com.example.demo.entity.CopyStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopyDTO {
  private Integer id;
  private CopyStatus status;
  private BookFormat format;
  private Integer bookId;
  private String bookTitle;

  public BookCopyDTO() {}

  public BookCopyDTO(Integer id, CopyStatus status, BookFormat format) {
    this.id = id;
    this.status = status;
    this.format = format;
  }
}
