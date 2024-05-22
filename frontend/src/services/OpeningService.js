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

export async function fetchOpeningsPerYear(orgUnitCode, statusCode, entryDateStart, entryDateEnd) {
    let authToken = await getAuthIdToken();
    try {
        // Construct URL with optional parameters
        let url = 'https://nr-silva-test-backend.apps.silver.devops.gov.bc.ca/api/dashboard-metrics/submission-trends';
        if (orgUnitCode || statusCode || entryDateStart || entryDateEnd) {
            url += '?';
            if (orgUnitCode) url += `orgUnitCode=${orgUnitCode}&`;
            if (statusCode) url += `statusCode=${statusCode}&`;
            if (entryDateStart) url += `entryDateStart=${entryDateStart}&`;
            if (entryDateEnd) url += `entryDateEnd=${entryDateEnd}&`;
            // Remove trailing '&' if present
            url = url.replace(/&$/, '');
        }
        
        const response = await axios.get(url, {
            headers: {
                Authorization: `Bearer ${authToken}`
            }
        });

        const { data } = response;

        if (data && Array.isArray(data)) {
            // Format data for BarChartGrouped component
            const formattedData = data.map(item => ({
                group: "Openings",
                key: item.monthName,
                value: item.amount
            }));

            return formattedData;
        } else {
            console.log('No data found in the response.');
            return [];
        }
    } catch (error) {
        console.error('Error fetching openings per year:', error);
        throw error;
    }
}

export async function fetchFreeGrowingMilestones(orgUnitCode, clientNumber, entryDateStart, entryDateEnd) {
    let authToken = await getAuthIdToken();
    let url = 'https://nr-silva-test-backend.apps.silver.devops.gov.bc.ca/api/dashboard-metrics/free-growing-milestones';

    // Construct URL with optional parameters
    if (orgUnitCode || clientNumber || entryDateStart || entryDateEnd) {
        url += '?';
        if (orgUnitCode) url += `orgUnitCode=${orgUnitCode}&`;
        if (clientNumber) url += `clientNumber=${clientNumber}&`;
        if (entryDateStart) url += `entryDateStart=${entryDateStart}&`;
        if (entryDateEnd) url += `entryDateEnd=${entryDateEnd}&`;
        // Remove trailing '&' if present
        url = url.replace(/&$/, '');
    }
    console.log("the url being called:")
    console.log(url)

    try {
        const response = await axios.get(url, {
            headers: {
                Authorization: `Bearer ${authToken}`
            }
        });

        const { data } = response;

        if (data && Array.isArray(data)) {
            // Transform data for DonutChartView component
            const transformedData = data.map(item => ({
                group: item.label,
                value: item.amount
            }));

            return transformedData;
        } else {
            console.log('No data found in the response.');
            return [];
        }
    } catch (error) {
        console.error('Error fetching free growing milestones:', error);
        throw error;
    }
}

export async function fetchRecentActions() {
    let authToken = await getAuthIdToken();
    try {
        // Comment out the actual API call for now
        // const response = await axios.get('https://nr-silva-test-backend.apps.silver.devops.gov.bc.ca/api/dashboard-metrics/my-recent-actions/requests', {
        //     headers: {
        //         Authorization: `Bearer ${authToken}`
        //     }
        // });

        // Temporarily use the sample data for testing
        // const { data } = response;
        const data = [
          {
            "activityType": "Update",
            "openingId": 1541297,
            "statusCode": "APP",
            "statusDescription": "Approved",
            "lastUpdatedLabel": "1 minute ago",
            "lastUpdated": "2024-05-16T19:59:21.635Z"
          },
          // Add more sample objects here if needed
        ];

        if (Array.isArray(data)) {
            // Transforming response data into a format consumable by the component
            const rows = data.map(action => ({
                activityType: action.activityType,
                openingID: action.openingId.toString(), // Convert openingId to string if needed
                status: action.statusDescription,
                lastUpdated: action.lastUpdatedLabel // Use lastUpdatedLabel from API
            }));
            
            // Returning the transformed data
            return rows;
        } else {
            console.log('No data found in the response.');
            return [];
        }
    } catch (error) {
        console.error('Error fetching recent actions:', error);
        throw error;
    }
}