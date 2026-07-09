package com.example.demo.dto;

import com.example.demo.entity.BookFormat;
import com.example.demo.entity.CopyStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopyDTO {
  private int id;
  private BookDTO book;
  private CopyStatus status;
  private BookFormat format;

  public BookCopyDTO() {}

  public BookCopyDTO(int id, BookDTO book, CopyStatus status, BookFormat format) {
    this.id = id;
    this.book = book;
    this.status = status;
    this.format = format;
  }
}
