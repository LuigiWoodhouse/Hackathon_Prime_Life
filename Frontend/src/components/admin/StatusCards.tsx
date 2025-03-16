import React from 'react';

interface StatusCardsProps {
  stats: {
    scheduledCount: number;
    completedCount: number;
    cancelledCount: number;
  };
}

export const StatusCards: React.FC<StatusCardsProps> = ({ stats }) => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      <div className="bg-white rounded-lg p-6 shadow-sm">
        <div className="flex items-center justify-between">
          <div>
            <div className="text-sm font-medium text-gray-500">Scheduled</div>
            <div className="text-2xl font-semibold text-blue-600 mt-1">
              {stats.scheduledCount}
            </div>
          </div>
          <div className="bg-blue-100 p-3 rounded-full">
            <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
        </div>
      </div>
      <div className="bg-white rounded-lg p-6 shadow-sm">
        <div className="flex items-center justify-between">
          <div>
            <div className="text-sm font-medium text-gray-500">Completed</div>
            <div className="text-2xl font-semibold text-green-600 mt-1">
              {stats.completedCount}
            </div>
          </div>
          <div className="bg-green-100 p-3 rounded-full">
            <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7" />
            </svg>
          </div>
        </div>
      </div>
      <div className="bg-white rounded-lg p-6 shadow-sm">
        <div className="flex items-center justify-between">
          <div>
            <div className="text-sm font-medium text-gray-500">Cancelled</div>
            <div className="text-2xl font-semibold text-red-600 mt-1">
              {stats.cancelledCount}
            </div>
          </div>
          <div className="bg-red-100 p-3 rounded-full">
            <svg className="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </div>
        </div>
      </div>
    </div>
  );
}; 