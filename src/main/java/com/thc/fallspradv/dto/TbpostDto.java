package com.thc.fallspradv.dto;

import com.thc.fallspradv.domain.Tbpost;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

public class TbpostDto {
    @Builder
    @Schema
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateReqDto{
        @Schema(description = "title", example="")
        @NotNull
        @NotEmpty
        private String title;
        @Schema(description = "content", example="")
        @NotNull
        @NotEmpty
        private String content;
        @Schema(description = "author", example="")
        @NotNull
        @NotEmpty
        private String author;

        private String tbuserId;

        public Tbpost toEntity(){
            return Tbpost.of(title, content, author, tbuserId);
        }
    }
    @Builder
    @Schema
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateResDto{
        private String id;
    }

    @SuperBuilder
    @Schema
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateReqDto extends DefaultDto.UpdateReqDto{
        @Schema(description = "title", example="")
        private String title;
        @Schema(description = "content", example="")
        private String content;
        @Schema(description = "author", example="")
        private String author;
    }

    //여기는 빌더 붙이면 에러 나요!! 조심!!
    @Schema
    @Getter
    @Setter
    public static class DetailResDto extends DefaultDto.DetailResDto{
        @Schema(description = "title", example="")
        private String title;
        @Schema(description = "content", example="")
        private String content;
        @Schema(description = "author", example="")
        private String author;
        @Schema(description = "tbuserId", example="")
        private String tbuserId;
        @Schema(description = "tbuserUsername", example="")
        private String tbuserUsername;
        @Schema(description = "tbuserName", example="")
        private String tbuserName;
    }

    @SuperBuilder
    @Schema
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListReqDto extends DefaultDto.ListReqDto{
        @Schema(description = "title", example="")
        private String title;
    }

    @SuperBuilder
    @Schema
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PagedListReqDto extends DefaultDto.PagedListReqDto{
        @Schema(description = "title", example="")
        private String title;
    }

    @SuperBuilder
    @Schema
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScrollListReqDto extends DefaultDto.ScrollListReqDto{
        @Schema(description = "title", example="")
        private String title;
        @Schema(description = "tbuserId", example="")
        private String tbuserId;
        @Schema(description = "tbuserUsername", example="")
        private String tbuserUsername;

        private String reqTbuserId;
    }

}
