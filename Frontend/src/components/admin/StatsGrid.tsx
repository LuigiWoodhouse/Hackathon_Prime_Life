import React from 'react';

interface StatsGridProps {
  stats: {
    totalDoctors: number;
    todayCount: number;
    upcomingCount: number;
    departmentsCount: number;
  };
}

export const StatsGrid: React.FC<StatsGridProps> = ({ stats }) => {
  const StatCard = ({ title, value, gradient }: { title: string; value: number; gradient: string }) => (
    <div className={`${gradient} rounded-lg p-4 sm:p-6 text-white transition-transform hover:scale-[1.02] duration-200`}>
      <div className="text-sm font-medium opacity-80">{title}</div>
      <div className="text-2xl sm:text-3xl font-bold mt-2">{value}</div>
    </div>
  );

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-6 mb-6 sm:mb-8">
      <StatCard
        title="Total Doctors"
        value={stats.totalDoctors}
        gradient="bg-gradient-to-br from-[#1e4d90] to-[#1e3a6a]"
      />
      <StatCard
        title="Today's Appointments"
        value={stats.todayCount}
        gradient="bg-gradient-to-br from-green-500 to-green-600"
      />
      <StatCard
        title="Upcoming"
        value={stats.upcomingCount}
        gradient="bg-gradient-to-br from-purple-500 to-purple-600"
      />
      <StatCard
        title="Departments"
        value={stats.departmentsCount}
        gradient="bg-gradient-to-br from-blue-500 to-blue-600"
      />
    </div>
  );
}; 