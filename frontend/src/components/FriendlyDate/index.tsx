import React from 'react';
import { formatDistanceToNow, format, parseISO, isToday, isYesterday } from 'date-fns';
import { Tooltip } from '@carbon/react';

interface FriendlyDateProps {
  date: string; // The date string in ISO format
}

const FriendlyDate: React.FC<FriendlyDateProps> = ({ date }) => {
  
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
};

export default FriendlyDate;