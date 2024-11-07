import React from 'react';
import { render, screen, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { NotificationProvider, useNotification } from '../../contexts/NotificationProvider';
import { NotificationContent } from '../../types/NotificationType';
import { ActionableNotification } from '@carbon/react';

// Mock the ActionableNotification component
vi.mock('@carbon/react', () => ({
  ActionableNotification: ({ title, subtitle, onClose, onActionButtonClick }: any) => (
    <div>
      <div>{title}</div>
      <div>{subtitle}</div>
      <button onClick={onClose}>Close</button>
      <button onClick={onActionButtonClick}>Action</button>
    </div>
  )
}));

const onCloseMock = vi.fn();

const TestComponent = () => {
  const { displayNotification } = useNotification();
  return (
    <button
      onClick={() =>
        displayNotification({
          title: 'Test Title',
          subTitle: 'Test Subtitle',
          type: 'info',
          buttonLabel: 'Test Button',
          dismissIn: 10,
          onClose: onCloseMock
        })
      }
    >
      Show Notification
    </button>
  );
};

describe('NotificationProvider', () => {
  it('should display notification when displayNotification is called', () => {
    render(
      <NotificationProvider>
        <TestComponent />
      </NotificationProvider>
    );

    act(() => {
      screen.getByText('Show Notification').click();
    });

    expect(screen.getByText('Test Title')).toBeInTheDocument();
    expect(screen.getByText('Test Subtitle')).toBeInTheDocument();
    expect(screen.getByText('Close')).toBeInTheDocument();
    expect(screen.getByText('Action')).toBeInTheDocument();
  });

  it('should hide notification when close button is clicked', () => {
    render(
      <NotificationProvider>
        <TestComponent />
      </NotificationProvider>
    );

    act(() => {
      screen.getByText('Show Notification').click();
    });

    act(() => {
      screen.getByText('Close').click();
    });

    expect(screen.queryByText('Test Title')).not.toBeInTheDocument();
    expect(screen.queryByText('Test Subtitle')).not.toBeInTheDocument();
  });

  it('should call onClose and hide notification when action button is clicked', () => {
    

    render(
      <NotificationProvider>
        <TestComponent />
      </NotificationProvider>
    );

    act(() => {
      screen.getByText('Show Notification').click();
    });

    act(() => {
      screen.getByText('Action').click();
    });

    expect(onCloseMock).toHaveBeenCalled();
    expect(screen.queryByText('Test Title')).not.toBeInTheDocument();
    expect(screen.queryByText('Test Subtitle')).not.toBeInTheDocument();
  });

  it('should hide notification after dismissIn time', async () => {
    vi.useFakeTimers(); // Use fake timers

    await act(async () => render(
      <NotificationProvider>
        <TestComponent />
      </NotificationProvider>
    ));

    await act(async () => {
      screen.getByText('Show Notification').click();
    });

    expect(screen.getByText('Test Title')).toBeInTheDocument();
    expect(screen.getByText('Test Subtitle')).toBeInTheDocument();

    await act(async () => vi.advanceTimersByTime(1111)); // Fast-forward time by 1 second

    expect(screen.queryByText('Test Title')).not.toBeInTheDocument();
    expect(screen.queryByText('Test Subtitle')).not.toBeInTheDocument

  });


  afterEach(() => {
    vi.useRealTimers(); // Restore real timers after each test
  });
});