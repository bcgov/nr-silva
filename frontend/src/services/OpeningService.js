import axios from 'axios';
import { getAuthIdToken } from './AuthService';

export async function fetchRecentOpenings() {
    let authToken = getAuthIdToken();
    try {
        const response = await axios.get('https://nr-silva-test-backend.apps.silver.devops.gov.bc.ca/api/openings/recent-openings?page=0&perPage=100', {
            headers: {
                Authorization: `Bearer ${authToken}`
            }
        });

        const { data } = response;

        if (data && data.data) {
            // Extracting row information from the fetched data
            const rows = data.data.map(opening => ({
                id: opening.openingId.toString(),
                openingId: opening.openingId.toString(),
                fileId: opening.fileId ? opening.fileId : '-',
                cuttingPermit: opening.cuttingPermit ? opening.cuttingPermit : '-',
                timberMark: opening.timberMark ? opening.timberMark : '-',
                cutBlock: opening.cutBlock ? opening.cutBlock : '-',
                grossAreaHa: opening.grossAreaHa ? opening.grossAreaHa.toString() : '-',
                status: opening.status ? opening.status.description : '-',
                category: opening.category ? opening.category.code : '-',
                disturbanceStart: opening.disturbanceStart ? opening.disturbanceStart : '-',
                createdAt: opening.entryTimestamp ? opening.entryTimestamp.split('T')[0] : '-',
                lastViewed: opening.updateTimestamp ? opening.updateTimestamp.split('T')[0] : '-'
            }));
            // Returning the rows
            return rows;
        } else {
            console.log('No data found in the response.');
            return [];
        }
    } catch (error) {
        console.error('Error fetching recent openings:', error);
        throw error;
    }
}
