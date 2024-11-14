import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';
import { render, act, waitFor, screen } from '@testing-library/react';
import OpeningsTab from '../../components/OpeningsTab';
import { AuthProvider } from '../../contexts/AuthProvider';
import { getWmsLayersWhitelistUsers } from '../../services/SecretsService';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import PaginationProvider from '../../contexts/PaginationProvider';


vi.mock('../../services/SecretsService', () => ({
  getWmsLayersWhitelistUsers: vi.fn()
}));

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
    (getWmsLayersWhitelistUsers as vi.Mock).mockResolvedValue([{userName: 'TEST'}]);
    
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
    console.log(screen.debug());
    expect(screen.getByText('Recent openings')).toBeInTheDocument();
  });

  it('should have Hide map when the showSpatial is true',async () =>{
    (getWmsLayersWhitelistUsers as vi.Mock).mockResolvedValue([{userName: 'TEST'}]);
    
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
    console.log(screen.debug());
    expect(screen.getByText('Hide map')).toBeInTheDocument();
  });

});