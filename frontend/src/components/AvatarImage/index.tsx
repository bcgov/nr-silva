import React from 'react';
import './AvatarImage.scss';

type Size = 'small' | 'large';

interface AvatarImageProps {
  userName: string;
  size: Size;
}

const getInitials = (userName: string) => {
  const nameParts = userName.split(' ');
  if (nameParts.length >= 2) {
    return nameParts[0][0] + nameParts[1][0];
  } else if (nameParts.length === 1) {
    return nameParts[0][0];
  }
  return '';
};

/**
 * Renders an AvatarImage component.
 *
 * @component
 * @param {object} props - The component props.
 * @param {string} props.userName - The username to be used in the Avatar.
 * @param {Size} props.size - The size that should be rendered.
 * @returns {React.JSX.Element} The rendered AvatarImage component.
 */
function AvatarImage({ userName, size }: AvatarImageProps): React.JSX.Element {
  const initials = getInitials(userName);

  return (
    <div className={`profile-image ${size}`}>
      <div className="initials">{initials}</div>
    </div>
  );
};

export default AvatarImage;
