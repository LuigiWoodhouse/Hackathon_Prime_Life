export interface Appointment {
  id: string;
  patientName: string;
  date: string;
  time: string;
  doctor: string;
  department?: string;
  reason?: string;
  status: 'scheduled' | 'completed' | 'cancelled';
} 