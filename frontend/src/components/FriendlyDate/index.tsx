import React from 'react';
import { formatDistanceToNow, format, parseISO, isToday, isYesterday } from 'date-fns';
import { Tooltip } from '@carbon/react';

interface FriendlyDateProps {
  date: string | null | undefined; // The date string in ISO format
}

const FriendlyDate: React.FC<FriendlyDateProps> = ({ date }) => {

  if(!date) return <span data-testid="friendly-date"></span>;
  
  try{
  const parsedDate = parseISO(date);
  const cleanDate = format(parsedDate, "MMM dd, yyyy");

  const formattedDate = isToday(parsedDate)
    ? "Today"
    : isYesterday(parsedDate)
    ? "Yesterday"
    : formatDistanceToNow(parsedDate, { addSuffix: true });

  return <Tooltip  
            label={cleanDate}
            align="bottom-left"
            autoAlign> 
            <span>{formattedDate}</span>
          </Tooltip>;
  } catch(e){
    return <span data-testid="friendly-date"></span>;
  }
};

export default FriendlyDate;