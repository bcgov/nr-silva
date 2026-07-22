import { useAuth } from '@/contexts/AuthProvider';
import Avatar from '@/components/Avatar';


const UserButton = () => {
  const { user } = useAuth();

  return (
    <div className="user-header-btn">
      <div className="avatar-and-org-name">
        <Avatar
          size="sm"
          initial={`${user?.firstName?.charAt(0).toUpperCase()}${user?.lastName?.charAt(0).toUpperCase()}`}
        />
      </div>
    </div>
  );
};

export default UserButton;
