import React from 'react';
import { 
  parseISO, 
  isFuture,
  format,
  differenceInMinutes, 
  differenceInHours, 
  differenceInDays 
} from 'date-fns';
import { Tooltip } from '@carbon/react';

interface FriendlyDateProps {
  date: string | null | undefined; // The date string in ISO format
}

const formatDateAtlassianStyle = (date: Date) => {
  const now = new Date();
  const minutesDiff = differenceInMinutes(now, date);
  const hoursDiff = differenceInHours(now, date);
  const daysDiff = differenceInDays(now, date);

  // Past dates
  if (minutesDiff < 1) return 'just now';
  if (minutesDiff < 60) return minutesDiff === 1 ? 'a minute ago' : `${minutesDiff} minutes ago`;
  if (hoursDiff < 24) return hoursDiff === 1 ? '1 hour ago' : `${hoursDiff} hours ago`;
  if (daysDiff === 1) return 'yesterday';
  if (daysDiff < 7) return `${daysDiff} days ago`;
  if(daysDiff === 7) return 'a week ago';
    
  // Use full date for older dates
  return format(date, 'MMMM d, yyyy');
}

// Future dates
const formatFutureDateAtlassianStyle = (date: Date) => {
  const now = new Date();
  const minutesDiff = differenceInMinutes(date, now);
  const hoursDiff = differenceInHours(date, now);
  const daysDiff = differenceInDays(date, now);

  if (minutesDiff < 1) return 'shortly';
  if (minutesDiff < 60) return minutesDiff === 1 ? 'in 1 minute' : `in ${minutesDiff} minutes`;
  if (hoursDiff < 24) return hoursDiff === 1 ? 'in 1 hour' : `in ${hoursDiff} hours`;
  if (daysDiff === 1) return 'tomorrow';
  if (daysDiff < 7) return `in ${daysDiff} days`;
  if (daysDiff === 7) return 'in a week';
  
  return format(date, 'MMMM d, yyyy');
}

const FriendlyDate: React.FC<FriendlyDateProps> = ({ date }) => {

  if (!date) return <span data-testid="friendly-date"></span>;

  try {
    const parsedDate = parseISO(date);
    const cleanDate = format(parsedDate, "MMMM dd, yyyy");

    // Use appropriate formatting for past or future dates
    const formattedDate = isFuture(parsedDate)
      ? formatFutureDateAtlassianStyle(parsedDate)
      : formatDateAtlassianStyle(parsedDate);

      if(cleanDate === formattedDate)
        console.log(`cleanDate: ${cleanDate} formattedDate: ${formattedDate}`);
    return (
      <>
      {(cleanDate === formattedDate) ? (<span>{formattedDate}</span>) : (
      <Tooltip  
        label={cleanDate} // Display full date in tooltip
        align="bottom-left"
        autoAlign
      > 
        <span>{formattedDate}</span>
      </Tooltip>
      )}
      </>
    );
  } catch (e) {
    return <span data-testid="friendly-date"></span>; // Fallback for invalid dates
  }
};

export default FriendlyDate;
