'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/context/AuthContext';
import { 
  mockAppointments, 
  mockUpdateAppointment, 
  mockDoctors,
  type Appointment 
} from '@/services/mockData';
import { StatsGrid } from '@/components/admin/StatsGrid';
import { StatusCards } from '@/components/admin/StatusCards';
import { AppointmentFilters } from '@/components/admin/AppointmentFilters';
import { AppointmentsTable } from '@/components/admin/AppointmentsTable';
import { Pagination } from '@/components/admin/Pagination';
import { DashboardHeader } from '@/components/admin/DashboardHeader';

export default function AdminDashboard() {
  const { user } = useAuth();
  const router = useRouter();
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [filteredAppointments, setFilteredAppointments] = useState<Appointment[]>([]);
  const [filterStatus, setFilterStatus] = useState<string>('all');
  const [searchTerm, setSearchTerm] = useState('');
  const [dateRange, setDateRange] = useState({ start: '', end: '' });
  const [selectedDepartment, setSelectedDepartment] = useState('all');
  const [activeTab, setActiveTab] = useState<'overview' | 'appointments'>('overview');
  const [showFilters, setShowFilters] = useState(false);
  
  // Pagination state
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    if (!user || user.role !== 'admin') {
      router.push('/login');
    }
  }, [user, router]);

  useEffect(() => {
    setAppointments(mockAppointments);
  }, []);

  const departments = [...new Set(mockAppointments.map(apt => apt.department))];
  const today = new Date().toISOString().split('T')[0];

  const stats = {
    totalDoctors: mockDoctors.length,
    todayCount: appointments.filter(apt => apt.date === today).length,
    upcomingCount: appointments.filter(apt => apt.date > today).length,
    completedCount: appointments.filter(apt => apt.status === 'completed').length,
    scheduledCount: appointments.filter(apt => apt.status === 'scheduled').length,
    cancelledCount: appointments.filter(apt => apt.status === 'cancelled').length,
    departmentsCount: departments.length
  };

  useEffect(() => {
    let filtered = [...appointments];

    if (filterStatus !== 'all') {
      filtered = filtered.filter(apt => apt.status === filterStatus);
    }

    if (selectedDepartment !== 'all') {
      filtered = filtered.filter(apt => apt.department === selectedDepartment);
    }

    if (searchTerm) {
      const searchLower = searchTerm.toLowerCase();
      filtered = filtered.filter(apt => 
        apt.patientName.toLowerCase().includes(searchLower) ||
        apt.doctor.toLowerCase().includes(searchLower) ||
        apt.patientEmail.toLowerCase().includes(searchLower) ||
        apt.department.toLowerCase().includes(searchLower)
      );
    }

    if (dateRange.start && dateRange.end) {
      filtered = filtered.filter(apt => 
        apt.date >= dateRange.start && apt.date <= dateRange.end
      );
    }

    setFilteredAppointments(filtered);
    setTotalPages(Math.ceil(filtered.length / itemsPerPage));
    setCurrentPage(1);
  }, [appointments, searchTerm, filterStatus, selectedDepartment, dateRange, itemsPerPage]);

  const updateAppointmentStatus = (id: string, status: 'scheduled' | 'completed' | 'cancelled') => {
    try {
      const updatedAppointment = mockUpdateAppointment(id, { status });
      setAppointments(appointments.map(apt => 
        apt.id === id ? updatedAppointment : apt
      ));
    } catch (error) {
      console.error('Error updating appointment:', error);
    }
  };

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentAppointments = filteredAppointments.slice(indexOfFirstItem, indexOfLastItem);

  if (!user || user.role !== 'admin') return null;

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 py-8">
        <DashboardHeader 
          activeTab={activeTab} 
          onTabChange={setActiveTab} 
        />

        {activeTab === 'overview' && (
          <>
            <StatsGrid stats={stats} />
            <StatusCards stats={stats} />
          </>
        )}

        {activeTab === 'appointments' && (
          <>
            <AppointmentFilters
              searchTerm={searchTerm}
              onSearchChange={setSearchTerm}
              showFilters={showFilters}
              onToggleFilters={() => setShowFilters(!showFilters)}
              filterStatus={filterStatus}
              onStatusChange={setFilterStatus}
              selectedDepartment={selectedDepartment}
              onDepartmentChange={setSelectedDepartment}
              dateRange={dateRange}
              onDateRangeChange={(start, end) => setDateRange({ start, end })}
              departments={departments}
              totalResults={filteredAppointments.length}
            />

            <AppointmentsTable
              appointments={currentAppointments}
              onUpdateStatus={updateAppointmentStatus}
            />

            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              totalItems={filteredAppointments.length}
              itemsPerPage={itemsPerPage}
              onPageChange={setCurrentPage}
            />
          </>
        )}
      </div>
    </div>
  );
} 