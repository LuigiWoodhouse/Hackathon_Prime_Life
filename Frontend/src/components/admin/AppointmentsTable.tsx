import React from 'react';
import { Appointment } from '@/services/mockData';

interface AppointmentsTableProps {
  appointments: Appointment[];
  onUpdateStatus: (id: string, status: 'scheduled' | 'completed' | 'cancelled') => void;
}

export const AppointmentsTable: React.FC<AppointmentsTableProps> = ({
  appointments,
  onUpdateStatus,
}) => {
  const getStatusBadgeClass = (status: string) => {
    switch (status) {
      case 'scheduled': return 'bg-blue-100 text-blue-800';
      case 'completed': return 'bg-green-100 text-green-800';
      case 'cancelled': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  // Mobile card view
  const MobileAppointmentCard = ({ appointment }: { appointment: Appointment }) => (
    <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-200 mb-4">
      <div className="flex justify-between items-start mb-3">
        <div>
          <h3 className="font-medium text-gray-900">{appointment.patientName}</h3>
          <p className="text-sm text-gray-500">{appointment.patientEmail}</p>
          <p className="text-sm text-gray-500">{appointment.patientPhone}</p>
        </div>
        <span className={`px-2 py-1 text-xs font-semibold rounded-full ${getStatusBadgeClass(appointment.status)}`}>
          {appointment.status.charAt(0).toUpperCase() + appointment.status.slice(1)}
        </span>
      </div>
      
      <div className="border-t border-gray-200 -mx-4 px-4 pt-3">
        <div className="grid grid-cols-2 gap-4 mb-3">
          <div>
            <p className="text-xs text-gray-500">Doctor</p>
            <p className="text-sm font-medium">{appointment.doctor}</p>
          </div>
          <div>
            <p className="text-xs text-gray-500">Department</p>
            <p className="text-sm font-medium">{appointment.department}</p>
          </div>
        </div>
        
        <div className="grid grid-cols-2 gap-4 mb-4">
          <div>
            <p className="text-xs text-gray-500">Date</p>
            <p className="text-sm font-medium">
              {new Date(appointment.date).toLocaleDateString()}
            </p>
          </div>
          <div>
            <p className="text-xs text-gray-500">Time</p>
            <p className="text-sm font-medium">{appointment.time}</p>
          </div>
        </div>

        <select
          value={appointment.status}
          onChange={(e) => onUpdateStatus(
            appointment.id,
            e.target.value as 'scheduled' | 'completed' | 'cancelled'
          )}
          className="w-full text-sm border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#1e4d90] focus:border-transparent"
        >
          <option value="scheduled">Mark Scheduled</option>
          <option value="completed">Mark Completed</option>
          <option value="cancelled">Mark Cancelled</option>
        </select>
      </div>
    </div>
  );

  return (
    <div className="bg-white rounded-lg shadow-sm overflow-hidden">
      {/* Mobile view */}
      <div className="md:hidden p-4">
        {appointments.map((appointment) => (
          <MobileAppointmentCard key={appointment.id} appointment={appointment} />
        ))}
      </div>

      {/* Desktop view */}
      <div className="hidden md:block overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Patient
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Doctor & Department
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Date & Time
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Status
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {appointments.map((appointment) => (
              <tr key={appointment.id} className="hover:bg-gray-50">
                <td className="px-6 py-4">
                  <div className="text-sm font-medium text-gray-900">
                    {appointment.patientName}
                  </div>
                  <div className="text-sm text-gray-500">
                    {appointment.patientEmail}
                  </div>
                  <div className="text-sm text-gray-500">
                    {appointment.patientPhone}
                  </div>
                </td>
                <td className="px-6 py-4">
                  <div className="text-sm font-medium text-gray-900">
                    {appointment.doctor}
                  </div>
                  <div className="text-sm text-gray-500">
                    {appointment.department}
                  </div>
                </td>
                <td className="px-6 py-4">
                  <div className="text-sm text-gray-900">
                    {new Date(appointment.date).toLocaleDateString()}
                  </div>
                  <div className="text-sm text-gray-500">
                    {appointment.time}
                  </div>
                </td>
                <td className="px-6 py-4">
                  <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusBadgeClass(appointment.status)}`}>
                    {appointment.status.charAt(0).toUpperCase() + appointment.status.slice(1)}
                  </span>
                </td>
                <td className="px-6 py-4">
                  <select
                    value={appointment.status}
                    onChange={(e) => onUpdateStatus(
                      appointment.id,
                      e.target.value as 'scheduled' | 'completed' | 'cancelled'
                    )}
                    className="text-sm border border-gray-300 rounded-md px-3 py-1 focus:outline-none focus:ring-2 focus:ring-[#1e4d90] focus:border-transparent"
                  >
                    <option value="scheduled">Mark Scheduled</option>
                    <option value="completed">Mark Completed</option>
                    <option value="cancelled">Mark Cancelled</option>
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}; 