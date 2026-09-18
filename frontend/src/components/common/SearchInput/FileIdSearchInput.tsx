import { useRef } from 'react';
import { Column, TextInput } from '@carbon/react';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { handleAutoUpperInput, handleAutoUpperPaste } from '@/utils/InputUtils';
import { FILE_ID_MAX_LENGTH } from '@/constants';

export type FileIdSearchInputProps = {
  value?: string;
  onChange: (value?: string) => void;
  id?: string;
  name?: string;
  labelText?: string;
  placeholder?: string;
};

export const FileIdSearchInput = ({
  value,
  onChange,
  id = 'file-id-input',
  name = 'file-id',
  labelText = 'File ID',
  placeholder = 'Enter file ID',
}: FileIdSearchInputProps) => {
  const inputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(inputRef, value);

  return (
    <Column sm={4} md={4} lg={6} max={4}>
      <TextInput
        ref={inputRef}
        id={id}
        name={name}
        labelText={labelText}
        placeholder={placeholder}
        defaultValue={value ?? ''}
        onInput={(e) => handleAutoUpperInput(e, FILE_ID_MAX_LENGTH)}
        onPaste={(e) => handleAutoUpperPaste(e, FILE_ID_MAX_LENGTH)}
        onBlur={(e) => onChange(e.target.value ? e.target.value : undefined)}
      />
    </Column>
  );
};

export default FileIdSearchInput;
