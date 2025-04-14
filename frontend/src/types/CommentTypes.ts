import CodeDescriptionDto from "./CodeDescriptionType"

export type CommentDto = {
  commentSource: CodeDescriptionDto,
  CodeDescriptionDto: CodeDescriptionDto,
  commentText: string | null
}
