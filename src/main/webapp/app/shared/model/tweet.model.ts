export interface ITweet {
  id?: string;
  tweeter?: string;
  content?: string;
}

export const defaultValue: Readonly<ITweet> = {};
