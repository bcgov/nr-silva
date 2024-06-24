import History from "../types/History";

const OpeningHistoryItems: History[] = [
  {
    id: 1541297,
    steps: [
      {
        step: 5,
        status: 'invalid',
        description: 'Milestone overdue',
        subtitle: 'Please, update the milestone'
      },
      {
        step: 4,
        status: 'complete',
        description: 'Activity completed',
        subtitle: '2023-11-15'
      },
      {
        step: 3,
        status: 'complete',
        description: 'Forest cover polygon',
        subtitle: '2023-11-02'
      },
      {
        step: 2,
        status: 'complete',
        description: 'Disturbance activity',
        subtitle: '2023-10-30'
      },
      {
        step: 1,
        status: 'complete',
        description: 'Opening ID',
        subtitle: '2023-10-18'
      }
    ]
  },
  {
    id: 1541298,
    steps: [
      {
        step: 5,
        status: 'complete',
        description: 'Forest Cover',
        subtitle: 'Now'
      },
      {
        step: 4,
        status: 'complete',
        description: 'Activity Planned',
        subtitle: 'Now'
      },
      {
        step: 3,
        status: 'complete',
        description: 'Forest cover polygon',
        subtitle: '2023-10-31'
      },
      {
        step: 2,
        status: 'complete',
        description: 'Disturbance Activity',
        subtitle: '2023-10-19'
      },
      {
        step: 1,
        status: 'complete',
        description: 'Opening ID',
        subtitle: '2023-10-01'
      }
    ]
  },
  {
    id: 1541299,
    steps: [
      {
        step: 5,
        status: 'invalid',
        description: 'Forest cover polygon',
        subtitle: 'Please update the forest cover'
      },
      {
        step: 4,
        status: 'invalid',
        description: 'Forest cover polygon',
        subtitle: 'PLease update the forest cover'
      },
      {
        step: 3,
        status: 'complete',
        description: 'Forest cover polygon',
        subtitle: '2023-11-01'
      },
      {
        step: 2,
        status: 'complete',
        description: 'Disturbance Activity',
        subtitle: '2023-10-29'
      },
      {
        step: 1,
        status: 'complete',
        description: 'Opening ID',
        subtitle: '2023-10-17'
      }
    ]
  }
];

export default OpeningHistoryItems;