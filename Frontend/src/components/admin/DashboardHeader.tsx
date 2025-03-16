import React from 'react';

interface DashboardHeaderProps {
  activeTab: 'overview' | 'appointments';
  onTabChange: (tab: 'overview' | 'appointments') => void;
}

export const DashboardHeader: React.FC<DashboardHeaderProps> = ({
  activeTab,
  onTabChange,
}) => {
  return (
    <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center mb-6 sm:mb-8 space-y-4 sm:space-y-0">
      <div>
        <h1 className="text-2xl sm:text-3xl font-bold text-gray-900">Admin Dashboard</h1>
        <p className="text-sm sm:text-base text-gray-600 mt-1">Manage appointments and view statistics</p>
      </div>
      <div className="flex w-full sm:w-auto">
        <button
          onClick={() => onTabChange('overview')}
          className={`flex-1 sm:flex-none px-4 py-2 rounded-l-md text-sm font-medium transition-colors ${
            activeTab === 'overview'
              ? 'bg-[#1e4d90] text-white'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
        >
          Overview
        </button>
        <button
          onClick={() => onTabChange('appointments')}
          className={`flex-1 sm:flex-none px-4 py-2 rounded-r-md text-sm font-medium transition-colors border-l border-gray-200 ${
            activeTab === 'appointments'
              ? 'bg-[#1e4d90] text-white'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
        >
          Appointments
        </button>
      </div>
    </div>
  );
}; 