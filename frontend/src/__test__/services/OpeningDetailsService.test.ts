import { describe, expect, it, vi, beforeEach } from 'vitest';
import axios from 'axios';
import { fetchOpeningTombstone } from '../../services/OpeningDetailsService';
import { getAuthIdToken } from '../../services/AuthService';
import { API_ENDPOINTS } from '../../services/apiConfig';
import '@testing-library/jest-dom';
import { OpeningDetailsTombstoneOverviewDto } from '../../types/OpenApiTypes';

vi.mock('axios');
vi.mock('../../services/AuthService');

describe('OpeningDetailsService', () => {
  const authToken = 'test-token';
  const openingId = 101017;

  beforeEach(() => {
    vi.clearAllMocks();
    // @ts-expect-error: getAuthIdToken is mocked by vi
    (getAuthIdToken as vi.Mock).mockReturnValue(authToken);
  });

  describe('fetchOpeningTombstone', () => {
    it('should fetch opening tombstone details successfully', async () => {
      const mockData: OpeningDetailsTombstoneOverviewDto = {
        openingId: openingId,
        tombstone: {
          openingNumber: " 92K 014 0.0  514",
          openingStatus: { code: "FG", description: "Free Growing" },
          orgUnitCode: "DAS",
          orgUnitName: "Development Unit",
          openCategory: { code: "FTML", description: "Forest Tenure - Major Licensee" },
          client: {
            clientNumber: "00010003",
            clientName: "Forest Client Name",
            legalFirstName: "John",
            legalMiddleName: "A.",
            clientStatusCode: { code: "ACTIVE", description: "Active Client" },
            clientTypeCode: { code: "LICENSEE", description: "Licensee" },
            acronym: "FCN"
          },
          fileId: "TFL47",
          cutBlockID: null,
          cuttingPermitId: "12K",
          timberMark: "47/12K",
          maxAllowedAccess: "7.8",
          openingGrossArea: 16.6,
          createdBy: "BABAYAGA",
          createdOn: "2001-06-07",
          lastUpdatedOn: "2014-04-02",
          disturbanceStartDate: "2001-09-18"
        },
        overview: {
          opening: {
            licenseeId: "012345678",
            tenureType: { code: "A02", description: "Tree Farm Licence" },
            managementUnitType: { code: "T", description: "TREE FARM LICENCE" },
            managementUnitId: "47",
            timberSaleOffice: { code: "LSB", description: "Lumber Sale Branch" },
            comments: [
              {
                commentSource: { code: "OPEN", description: "Opening" },
                commentType: { code: "GENERAL", description: "General Comments" },
                commentText: "All good so far"
              }
            ]
          },
          milestones: {
            standardsUnitId: "A",
            postHarvestDeclaredDate: "2004-09-18",
            regenDeclaredDate: "2004-09-18",
            regenOffsetYears: 3,
            regenDueDate: "2004-09-18",
            freeGrowingDeclaredDate: "2012-04-30",
            freeGrowingOffsetYears: 11,
            freeGrowingDueDate: "2012-09-18"
          }
        },
        notifications: [
          {
            "title": "Overdue milestone detected for standard unit \"A, B\"",
            "description": "Immediate action required!",
            "status": "ERROR"
          }
        ],
      };

      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const result = await fetchOpeningTombstone(openingId);

      expect(axios.get).toHaveBeenCalledWith(
        API_ENDPOINTS.openingTombstone(openingId),
        {
          headers: {
            Authorization: `Bearer ${authToken}`,
            "Access-Control-Allow-Origin": "http://localhost:3000",
            "Content-Type": "application/json"
          }
        }
      );
      expect(result).toEqual(mockData);
    });

    it('should handle error while fetching opening tombstone details', async () => {
      (axios.get as vi.Mock).mockRejectedValue(new Error('Failed to fetch opening details'));

      await expect(fetchOpeningTombstone(openingId)).rejects.toThrow('Failed to fetch opening details');
    });
  });
});
