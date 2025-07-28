import { useNavigate, useSearchParams } from 'react-router-dom';
import { TENURED_OPENING, GOV_FUNDED_OPENING } from '@/constants';

const CreateOpening = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const type = searchParams.get('type');

  const isValidType =
    type === TENURED_OPENING || type === GOV_FUNDED_OPENING;

  if (!isValidType) {
    console.warn("Invalid opening type");
    navigate("/");
  }

  return (
    <div>
      Creating: {type === TENURED_OPENING ? 'Tenured' : 'Gov Funded'} Opening
    </div>
  );
};

export default CreateOpening;
