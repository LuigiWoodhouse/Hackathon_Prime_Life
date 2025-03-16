'use client';

import { useState } from 'react';
import { type Appointment } from '@/services/mockData';

interface AppointmentDashboardProps {
  appointments: Appointment[];
  userRole: 'admin' | 'doctor' | 'staff' | 'patient';
}

export function AppointmentDashboard({ appointments, userRole }: AppointmentDashboardProps) {
  const [showForm, setShowForm] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState<Appointment | null>(null);
  const [formData, setFormData] = useState({
    patientName: '',
    date: '',
    time: '',
    reason: '',
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: Implement appointment creation logic
    setFormData({ patientName: '', date: '', time: '', reason: '' });
    setShowForm(false);
  };

  const handleViewDetails = (appointment: Appointment) => {
    setSelectedAppointment(appointment);
    setShowDetailsModal(true);
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center bg-white p-6 rounded-lg shadow-sm">
        <div>
          <h2 className="text-2xl font-semibold text-[#1e4d90]">Appointments</h2>
          <p className="text-gray-600 text-sm mt-1">
            {userRole === 'doctor' 
              ? 'Manage your patient appointments'
              : userRole === 'patient'
              ? 'View your appointments'
              : 'View all appointments'}
          </p>
        </div>
        {userRole !== 'admin' && (
          <button
            onClick={() => setShowForm(true)}
            className="bg-[#1e4d90] text-white px-6 py-3 rounded-lg hover:bg-[#163c72] transition-colors flex items-center gap-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path fillRule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clipRule="evenodd" />
            </svg>
            New Appointment
          </button>
        )}
      </div>

      {/* Appointment Details Modal */}
      {showDetailsModal && selectedAppointment && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-full max-w-2xl shadow-lg rounded-lg bg-white">
            <div className="flex justify-between items-center border-b pb-4">
              <h3 className="text-xl font-semibold text-[#1e4d90]">Appointment Details</h3>
              <button
                onClick={() => setShowDetailsModal(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <svg className="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            <div className="mt-4 space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-500">Patient Name</label>
                  <p className="mt-1 text-sm text-gray-900">{selectedAppointment.patientName}</p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-500">Doctor</label>
                  <p className="mt-1 text-sm text-gray-900">{selectedAppointment.doctor}</p>
                </div>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-500">Date</label>
                  <p className="mt-1 text-sm text-gray-900">
                    {new Date(selectedAppointment.date).toLocaleDateString()}
                  </p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-500">Time</label>
                  <p className="mt-1 text-sm text-gray-900">{selectedAppointment.time}</p>
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-500">Department</label>
                <p className="mt-1 text-sm text-gray-900">{selectedAppointment.department}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-500">Reason for Visit</label>
                <p className="mt-1 text-sm text-gray-900">{selectedAppointment.reason}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-500">Contact Information</label>
                <p className="mt-1 text-sm text-gray-900">
                  Email: {selectedAppointment.patientEmail}<br />
                  Phone: {selectedAppointment.patientPhone}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-500">Status</label>
                <span className={`mt-2 inline-block px-3 py-1 rounded-full text-xs font-medium ${
                  selectedAppointment.status === 'scheduled' ? 'bg-blue-100 text-blue-800' :
                  selectedAppointment.status === 'completed' ? 'bg-green-100 text-green-800' :
                  'bg-red-100 text-red-800'
                }`}>
                  {selectedAppointment.status.charAt(0).toUpperCase() + selectedAppointment.status.slice(1)}
                </span>
              </div>
              {selectedAppointment.notes && (
                <div>
                  <label className="block text-sm font-medium text-gray-500">Additional Notes</label>
                  <p className="mt-1 text-sm text-gray-900">{selectedAppointment.notes}</p>
                </div>
              )}
            </div>
            <div className="mt-6 flex justify-end">
              <button
                onClick={() => setShowDetailsModal(false)}
                className="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Appointment Form Modal */}
      {showForm && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-full max-w-2xl shadow-lg rounded-lg bg-white">
            <div className="flex justify-between items-center border-b pb-4">
              <h3 className="text-xl font-semibold text-[#1e4d90]">Schedule New Appointment</h3>
              <button
                onClick={() => setShowForm(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <svg className="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            <form onSubmit={handleSubmit} className="space-y-4 mt-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Patient Name</label>
                <input
                  type="text"
                  value={formData.patientName}
                  onChange={(e) => setFormData({ ...formData, patientName: e.target.value })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-[#1e4d90] focus:ring-[#1e4d90]"
                  required
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Date</label>
                  <input
                    type="date"
                    value={formData.date}
                    onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-[#1e4d90] focus:ring-[#1e4d90]"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Time</label>
                  <input
                    type="time"
                    value={formData.time}
                    onChange={(e) => setFormData({ ...formData, time: e.target.value })}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-[#1e4d90] focus:ring-[#1e4d90]"
                    required
                  />
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Reason for Visit</label>
                <textarea
                  value={formData.reason}
                  onChange={(e) => setFormData({ ...formData, reason: e.target.value })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-[#1e4d90] focus:ring-[#1e4d90]"
                  rows={3}
                  required
                />
              </div>
              <div className="flex justify-end space-x-3 pt-4 border-t">
                <button
                  type="button"
                  onClick={() => setShowForm(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-6 py-2 bg-[#1e4d90] text-white rounded-md hover:bg-[#163c72]"
                >
                  Schedule Appointment
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-sm overflow-hidden">
        {appointments.length === 0 ? (
          <div className="p-8 text-center">
            <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            <h3 className="mt-2 text-sm font-medium text-gray-900">No appointments</h3>
            <p className="mt-1 text-sm text-gray-500">
              {userRole === 'patient' 
                ? "Schedule an appointment with one of our doctors."
                : userRole === 'doctor'
                ? "No appointments scheduled."
                : "No appointments in the system."}
            </p>
            {userRole !== 'admin' && (
              <div className="mt-6">
                <button
                  onClick={() => setShowForm(true)}
                  className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-[#1e4d90] hover:bg-[#163c72]"
                >
                  <svg className="-ml-1 mr-2 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                  </svg>
                  Schedule Appointment
                </button>
              </div>
            )}
          </div>
        ) : (
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  {userRole === 'doctor' ? 'Patient Name' : 'Doctor'}
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Date
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Time
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Reason
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                {userRole === 'admin' && (
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Department
                  </th>
                )}
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {appointments.map((appointment) => (
                <tr key={appointment.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">
                      {userRole === 'doctor' ? appointment.patientName : appointment.doctor}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{appointment.date}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{appointment.time}</div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="text-sm text-gray-900">{appointment.reason}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      appointment.status === 'scheduled' ? 'bg-blue-100 text-blue-800' :
                      appointment.status === 'completed' ? 'bg-green-100 text-green-800' :
                      'bg-red-100 text-red-800'
                    }`}>
                      {appointment.status.charAt(0).toUpperCase() + appointment.status.slice(1)}
                    </span>
                  </td>
                  {userRole === 'admin' && (
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">{appointment.department}</div>
                    </td>
                  )}
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button
                      onClick={() => handleViewDetails(appointment)}
                      className="text-[#1e4d90] hover:text-[#163c72]"
                    >
                      View Details
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
} 