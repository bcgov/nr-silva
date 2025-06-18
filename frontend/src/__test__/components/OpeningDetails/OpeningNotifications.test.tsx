import React from 'react';
import { render, screen } from '@testing-library/react';
import OpeningNotifications from '../../../components/OpeningDetails/OpeningNotifications';
import { OpeningDetailsNotificationDto } from '../../../types/OpenApiTypes';

describe('OpeningNotifications', () => {
  const notifications: OpeningDetailsNotificationDto[] = [
    {
      title: 'Error Title',
      description: 'Error occurred',
      status: 'ERROR'
    },
    {
      title: 'Warning Title',
      description: 'This is a warning',
      status: 'WARNING'
    },
    {
      title: 'Info Title',
      description: 'Some information',
      status: 'INFO'
    },
    {
      title: 'Success Title',
      description: 'Operation successful',
      status: 'SUCCESS'
    }
  ];

  test('renders notifications in correct order and with correct content', () => {
    render(<OpeningNotifications notifications={notifications} />);

    // Should render all notifications
    notifications.forEach((notification, idx) => {
      expect(screen.getByTestId(`notification-${idx}`)).toBeInTheDocument();
      expect(screen.getByText(notification.title!)).toBeInTheDocument();
      expect(screen.getByText(notification.description!)).toBeInTheDocument();
    });

    // Should render in order: ERROR, WARNING, INFO, SUCCESS
    const renderedTitles = screen.getAllByRole('status').map(el => el.querySelector('.cds--inline-notification__title')?.textContent);
    expect(renderedTitles).toEqual([
      'Error Title',
      'Warning Title',
      'Info Title',
      'Success Title'
    ]);
  });

  test('renders nothing when notifications array is empty', () => {
    render(<OpeningNotifications notifications={[]} />);
    expect(screen.queryByRole('alert')).not.toBeInTheDocument();
  });

  test('handles missing title or description gracefully', () => {
    const incompleteNotifications: OpeningDetailsNotificationDto[] = [
      { title: null, description: null, status: 'INFO' }
    ];
    render(<OpeningNotifications notifications={incompleteNotifications} />);
    // Should render with empty title and subtitle
    expect(screen.getByTestId('notification-0')).toBeInTheDocument();
  });
});
