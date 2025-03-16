'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { AppointmentDashboard } from '../components/AppointmentDashboard';
import { useAuth } from '@/context/AuthContext';
import { mockAppointments, type Appointment } from '@/services/mockData';
import AppointmentForm from '@/components/AppointmentForm';

// Reusable components
const AppointmentCard = ({ appointment, user, onViewDetails }: { 
  appointment: Appointment; 
  user: any;
  onViewDetails: (appointment: Appointment) => void;
}) => (
  <div 
    className="flex items-center justify-between p-4 bg-gray-50 rounded-lg border border-gray-100 hover:bg-gray-100 cursor-pointer transition-colors"
    onClick={() => onViewDetails(appointment)}
  >
    <div className="flex items-center space-x-4">
      <div className="flex-shrink-0">
        <div className="w-12 h-12 rounded-full bg-blue-100 text-[#1e4d90] flex items-center justify-center">
          {user.role === 'doctor' 
            ? appointment.patientName.split(' ').map(n => n[0]).join('')
            : appointment.doctor.split(' ').map(n => n[0]).join('')
          }
        </div>
      </div>
      <div>
        <div className="font-medium text-gray-900">
          {user.role === 'doctor' ? appointment.patientName : appointment.doctor}
        </div>
        <div className="text-sm text-gray-500">
          {new Date(appointment.date).toLocaleDateString()} at {appointment.time}
        </div>
        <div className="text-sm text-gray-500">{appointment.reason}</div>
        {user.role === 'patient' && (
          <div className="text-sm text-gray-500">Department: {appointment.department}</div>
        )}
      </div>
    </div>
    <div className="flex items-center space-x-4">
      <span className={`px-3 py-1 rounded-full text-xs font-medium ${
        appointment.status === 'scheduled' ? 'bg-blue-100 text-blue-800' :
        appointment.status === 'completed' ? 'bg-green-100 text-green-800' :
        'bg-red-100 text-red-800'
      }`}>
        {appointment.status.charAt(0).toUpperCase() + appointment.status.slice(1)}
      </span>
      <button 
        className="text-gray-400 hover:text-gray-600"
        onClick={(e) => {
          e.stopPropagation();
          onViewDetails(appointment);
        }}
      >
        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 5v.01M12 12v.01M12 19v.01M12 6a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2z" />
        </svg>
      </button>
    </div>
  </div>
);

const EmptyState = ({ user, onSchedule }: { 
  user: any; 
  onSchedule: () => void;
}) => (
  <div className="text-center py-12">
    <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
    </svg>
    <h3 className="mt-2 text-sm font-medium text-gray-900">No appointments</h3>
    <p className="mt-1 text-sm text-gray-500">
      {user.role === 'patient' 
        ? "Schedule an appointment with one of our doctors."
        : user.role === 'doctor'
        ? "No appointments scheduled."
        : "No appointments in the system."}
    </p>
    {user.role !== 'admin' && (
      <div className="mt-6">
        <button
          onClick={onSchedule}
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
);

const AppointmentDetailsModal = ({ appointment, onClose }: {
  appointment: Appointment;
  onClose: () => void;
}) => (
  <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div className="relative top-20 mx-auto p-5 border w-full max-w-2xl shadow-lg rounded-lg bg-white">
      <div className="flex justify-between items-center border-b pb-4">
        <h3 className="text-xl font-semibold text-[#1e4d90]">Appointment Details</h3>
        <button
          onClick={onClose}
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
            <p className="mt-1 text-sm text-gray-900">{appointment.patientName}</p>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-500">Doctor</label>
            <p className="mt-1 text-sm text-gray-900">{appointment.doctor}</p>
          </div>
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-500">Date</label>
            <p className="mt-1 text-sm text-gray-900">
              {new Date(appointment.date).toLocaleDateString()}
            </p>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-500">Time</label>
            <p className="mt-1 text-sm text-gray-900">{appointment.time}</p>
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-500">Department</label>
          <p className="mt-1 text-sm text-gray-900">{appointment.department}</p>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-500">Reason for Visit</label>
          <p className="mt-1 text-sm text-gray-900">{appointment.reason}</p>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-500">Contact Information</label>
          <p className="mt-1 text-sm text-gray-900">
            Email: {appointment.patientEmail}<br />
            Phone: {appointment.patientPhone}
          </p>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-500">Status</label>
          <span className={`mt-2 inline-block px-3 py-1 rounded-full text-xs font-medium ${
            appointment.status === 'scheduled' ? 'bg-blue-100 text-blue-800' :
            appointment.status === 'completed' ? 'bg-green-100 text-green-800' :
            'bg-red-100 text-red-800'
          }`}>
            {appointment.status.charAt(0).toUpperCase() + appointment.status.slice(1)}
          </span>
        </div>
        {appointment.notes && (
          <div>
            <label className="block text-sm font-medium text-gray-500">Additional Notes</label>
            <p className="mt-1 text-sm text-gray-900">{appointment.notes}</p>
          </div>
        )}
      </div>
      <div className="mt-6 flex justify-end">
        <button
          onClick={onClose}
          className="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"
        >
          Close
        </button>
      </div>
    </div>
  </div>
);

export default function Home() {
  const { user } = useAuth();
  const router = useRouter();
  const [showAppointmentForm, setShowAppointmentForm] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState<Appointment | null>(null);
  const [activeTab, setActiveTab] = useState<'upcoming' | 'today' | 'all'>('upcoming');

  useEffect(() => {
    if (!user) {
      router.push('/login');
    }
  }, [user, router]);

  if (!user) {
    return null;
  }

  // Filter appointments based on user role
  const filterAppointments = () => {
    if (user.role === 'doctor') {
      return mockAppointments.filter(apt => apt.doctor === user.name);
    } else if (user.role === 'patient') {
      return mockAppointments.filter(apt => 
        apt.patientEmail === user.email || 
        apt.patientName === user.name
      );
    } else if (user.role === 'admin') {
      return mockAppointments; // Admins can see all appointments
    }
    return [];
  };

  const userAppointments = filterAppointments();

  // Get today's appointments
  const today = new Date().toISOString().split('T')[0];
  const todayAppointments = userAppointments.filter(apt => apt.date === today);

  // Get upcoming appointments (next 7 days)
  const nextWeek = new Date();
  nextWeek.setDate(nextWeek.getDate() + 7);
  const upcomingAppointments = userAppointments.filter(apt => 
    apt.date > today && 
    apt.date <= nextWeek.toISOString().split('T')[0]
  );

  const handleNewAppointment = (appointmentData: any) => {
    console.log('New appointment:', appointmentData);
    // Create a new appointment object with the submitted data
    const newAppointment: Appointment = {
      id: String(Date.now()), // Generate a temporary ID
      ...appointmentData,
      status: 'scheduled',
    };
    
    // Show the appointment details
    setSelectedAppointment(newAppointment);
    setShowAppointmentForm(false);
    setShowDetailsModal(true);
  };

  const handleViewDetails = (appointment: Appointment) => {
    setSelectedAppointment(appointment);
    setShowDetailsModal(true);
  };

  // Get role-specific welcome message
  const getWelcomeMessage = () => {
    switch (user.role) {
      case 'doctor':
        return 'Manage your patient appointments';
      case 'patient':
        return 'Manage your healthcare appointments';
      case 'admin':
        return 'Monitor all appointments';
      default:
        return 'Manage your healthcare journey with ease';
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section with Quick Actions */}
      <div className="bg-gradient-to-r from-[#1e4d90] to-[#1e3a6a] text-white">
        <div className="max-w-7xl mx-auto px-4 py-8">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center space-y-4 md:space-y-0">
            <div>
              <h1 className="text-3xl font-bold mb-2">Welcome back, {user.name}</h1>
              <p className="text-blue-100">{getWelcomeMessage()}</p>
            </div>
            {user.role !== 'admin' && (
              <button 
                onClick={() => setShowAppointmentForm(true)}
                className="bg-white text-[#1e4d90] px-6 py-3 rounded-lg font-medium hover:bg-blue-50 transition-colors duration-200 flex items-center space-x-2 shadow-sm"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                </svg>
                <span>Schedule Appointment</span>
              </button>
            )}
          </div>

          {/* Quick Stats */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-8">
            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-6">
              <div className="text-sm font-medium text-blue-100 mb-1">Today's Appointments</div>
              <div className="text-3xl font-bold">{todayAppointments.length}</div>
            </div>
            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-6">
              <div className="text-sm font-medium text-blue-100 mb-1">Upcoming (7 days)</div>
              <div className="text-3xl font-bold">{upcomingAppointments.length}</div>
            </div>
            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-6">
              <div className="text-sm font-medium text-blue-100 mb-1">
                {user.role === 'doctor' ? 'Department' : 'Total Appointments'}
              </div>
              <div className="text-3xl font-bold">
                {user.role === 'doctor' ? (user.department || 'General') : userAppointments.length}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="max-w-7xl mx-auto px-4 py-8">
        {/* Tabs */}
        <div className="flex space-x-1 bg-white p-1 rounded-lg shadow-sm mb-6">
          <button
            onClick={() => setActiveTab('upcoming')}
            className={`flex-1 py-2.5 px-4 rounded-md text-sm font-medium transition-colors ${
              activeTab === 'upcoming' 
                ? 'bg-[#1e4d90] text-white' 
                : 'text-gray-600 hover:bg-gray-100'
            }`}
          >
            Upcoming
          </button>
          <button
            onClick={() => setActiveTab('today')}
            className={`flex-1 py-2.5 px-4 rounded-md text-sm font-medium transition-colors ${
              activeTab === 'today' 
                ? 'bg-[#1e4d90] text-white' 
                : 'text-gray-600 hover:bg-gray-100'
            }`}
          >
            Today
          </button>
          <button
            onClick={() => setActiveTab('all')}
            className={`flex-1 py-2.5 px-4 rounded-md text-sm font-medium transition-colors ${
              activeTab === 'all' 
                ? 'bg-[#1e4d90] text-white' 
                : 'text-gray-600 hover:bg-gray-100'
            }`}
          >
            All Appointments
          </button>
        </div>

        {/* Content based on active tab */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-100">
          {activeTab === 'today' && (
            <div className="p-6">
              <h2 className="text-xl font-semibold text-gray-900 mb-4">Today's Schedule</h2>
              {/* {todayAppointments.length > 0 ? (
                <div className="space-y-3">
                  {todayAppointments.map(apt => (
                    <AppointmentCard
                      key={apt.id}
                      appointment={apt}
                      user={user}
                      onViewDetails={handleViewDetails}
                    />
                  ))}
                </div>
              ) : (
                <EmptyState user={user} onSchedule={() => setShowAppointmentForm(true)} />
              )} */}
            </div>
          )}

          {activeTab === 'upcoming' && (
            <div className="p-6">
              <h2 className="text-xl font-semibold text-gray-900 mb-4">Upcoming Appointments</h2>
              {/* {upcomingAppointments.length > 0 ? (
                <div className="space-y-3">
                  {upcomingAppointments.map(apt => (
                    <AppointmentCard
                      key={apt.id}
                      appointment={apt}
                      user={user}
                      onViewDetails={handleViewDetails}
                    />
                  ))}
                </div>
              ) : (
                <EmptyState user={user} onSchedule={() => setShowAppointmentForm(true)} />
              )} */}
            </div>
          )}

          {activeTab === 'all' && (
            <div className="p-6">
              <h2 className="text-xl font-semibold text-gray-900 mb-4">All Appointments</h2>
              {/* <AppointmentDashboard appointments={userAppointments} userRole={user.role} /> */}
            </div>
          )}
        </div>
      </div>

      {/* Modals */}
      {showAppointmentForm && (
        <AppointmentForm
          onClose={() => setShowAppointmentForm(false)}
          onSubmit={handleNewAppointment}
          user={user}
        />
      )}

      {showDetailsModal && selectedAppointment && (
        <AppointmentDetailsModal
          appointment={selectedAppointment}
          onClose={() => setShowDetailsModal(false)}
        />
      )}
    </div>
  );
} 