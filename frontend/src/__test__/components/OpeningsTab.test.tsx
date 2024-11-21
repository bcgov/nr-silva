import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';
import { render, act, waitFor, screen } from '@testing-library/react';
import OpeningsTab from '../../components/OpeningsTab';
import { AuthProvider } from '../../contexts/AuthProvider';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import PaginationProvider from '../../contexts/PaginationProvider';


vi.mock('../../services/OpeningService', async () => {
  const actual = await vi.importActual('../../services/OpeningService');
  return {
    ...actual,
    fetchRecentOpenings: vi.fn(),
  };
});
const queryClient = new QueryClient();

describe('Openings Tab test',() => {

  it('should render properly',async () =>{
        
    await act(async () => {
    render(
      <AuthProvider>
        <QueryClientProvider client={queryClient}>
          <PaginationProvider>
            <OpeningsTab showSpatial={false} setShowSpatial={vi.fn()} />
          </PaginationProvider>
        </QueryClientProvider>
      </AuthProvider>
    );
    });
    expect(screen.getByText('Recent openings')).toBeInTheDocument();
  });

  it('should have Hide map when the showSpatial is true',async () =>{
        
    await act(async () => {
    render(
      <AuthProvider>
        <QueryClientProvider client={queryClient}>
          <PaginationProvider>
            <OpeningsTab showSpatial={true} setShowSpatial={vi.fn()} />
          </PaginationProvider>
        </QueryClientProvider>
      </AuthProvider>
    );
    });
    expect(screen.getByText('Hide map')).toBeInTheDocument();
  });

  it('should render the table', async () => {
    // Mocking state values
    vi.spyOn(React, 'useState')
      .mockImplementationOnce(() => [null, vi.fn()])  // for loadId
      .mockImplementationOnce(() => [true, vi.fn()])  // for openingPolygonNotFound
      .mockImplementationOnce(() => [{ userName: 'TEST' }, vi.fn()])  // for wmsUsersWhitelist
      .mockImplementationOnce(() => [[], vi.fn()]);  // for headers
    
    await act(async () => {
      render(
        <AuthProvider>
          <QueryClientProvider client={queryClient}>
            <PaginationProvider>
              <OpeningsTab showSpatial={true} setShowSpatial={vi.fn()} />
            </PaginationProvider>
          </QueryClientProvider>
        </AuthProvider>
      );
    });
    expect(screen.getByRole('table')).toBeInTheDocument();
  });

});