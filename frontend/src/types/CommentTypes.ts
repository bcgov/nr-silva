import CodeDescriptionDto from "./CodeDescriptionType"

export type CommentDto = {
  commentSource: CodeDescriptionDto,
  codeDescriptionDto: CodeDescriptionDto,
  commentText: string | null
}
