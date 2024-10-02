package com.thc.fallspradv.dto;

import com.thc.fallspradv.domain.RefreshToken;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class RefreshTokenDto {
	@Schema
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateReqDto {
		@Schema(description = "content", example="content")
		@NotNull
		@NotEmpty
		@Size(max=2000)
		private String content;

		@Schema(description = "tbuserId", example="tbuserId")
		private String tbuserId;

		public RefreshToken toEntity() {
			return RefreshToken.of(content, tbuserId);
		}
	}
	@Schema
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class CreateResDto {
		@Schema(description = "id", example="length32textnumber")
		private String id;
	}

	@Schema
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class UpdateReqDto {
		@Schema(description = "id", example="length32textnumber")
		@NotNull
		@NotEmpty
		@Size(max=32)
		private String id;

		@Schema(description = "deleted", example="Y")
		@Size(max=1)
		private String deleted;

		@Schema(description = "content", example="content")
		@Size(max=2000)
		private String content;
	}
	@Schema
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class UpdateResDto {
		@Schema(description = "id", example="length32textnumber")
		private String id;
		@Schema(description = "deleted", example="Y")
		private String deleted;
		@Schema(description = "content", example="content")
		@Size(max=2000)
		private String content;
	}

	@Schema
	@Builder
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SelectResDto {

		@Schema(description = "id", example="id")
		private String id;
		@Schema(description = "content", example="content")
		private String content;
		@Schema(description = "deleted", example="N")
		private String deleted;
		@Schema(description = "created_at", example="2024-01-01 00:00:00.000000")
		private String created_at;
		@Schema(description = "modified_at", example="2024-01-01 00:00:00.000000")
		private String modified_at;

	}

}