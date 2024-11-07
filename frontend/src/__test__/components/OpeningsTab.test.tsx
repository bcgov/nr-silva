import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';
import { render, act, waitFor, screen } from '@testing-library/react';
import OpeningsTab from '../../components/OpeningsTab';
import { AuthProvider } from '../../contexts/AuthProvider';
import { getWmsLayersWhitelistUsers } from '../../services/SecretsService';
import { fetchRecentOpenings } from '../../services/OpeningService';
import { RecentOpening } from '../../types/RecentOpening';
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


const rows: RecentOpening[] = [{
  id: '123',
  openingId: '123',
  fileId: '1',
  cuttingPermit: '1',
  timberMark: '1',
  cutBlock: '1',
  grossAreaHa: 1,
  statusDesc: 'Approved',
  categoryDesc: 'Another:Another',
  disturbanceStart: '1',
  entryTimestamp: '1',
  updateTimestamp: '1',
}];

describe('Openings Tab test',() => {

  it('should render properly',async () =>{
    (getWmsLayersWhitelistUsers as vi.Mock).mockResolvedValue([{userName: 'TEST'}]);
    (fetchRecentOpenings as vi.Mock).mockResolvedValue(rows);
    await act(async () => {
    render(<AuthProvider><PaginationProvider><OpeningsTab showSpatial={false} setShowSpatial={vi.fn()}/></PaginationProvider></AuthProvider>);
    });
    expect(screen.getByText('Recent openings')).toBeInTheDocument();
  });

});