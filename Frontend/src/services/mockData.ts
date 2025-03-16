export interface Appointment {
  id: string;
  patientName: string;
  date: string;
  time: string;
  reason: string;
  status: 'scheduled' | 'completed' | 'cancelled';
  doctor: string;
  patientEmail: string;
  patientPhone: string;
  notes?: string;
  department: string;
}

export interface Session {
  id: string;
  userId: string;
  token: string;
  createdAt: string;
  expiresAt: string;
  isActive: boolean;
}

export interface User {
  id: string;
  name: string;
  email: string;
  role: 'admin' | 'doctor' | 'staff' | 'patient';
  department?: string;
  specialization?: string;
  phone?: string;
  sessions?: Session[];
  lastActiveSession?: string;
}

// Mock doctors data
export const mockDoctors: User[] = [
  {
    id: 'd1',
    name: 'Dr. Sarah Wilson',
    email: 'sarah.wilson@hospital.com',
    role: 'doctor',
    department: 'Cardiology',
    specialization: 'Interventional Cardiology'
  },
  {
    id: 'd2',
    name: 'Dr. Michael Brown',
    email: 'michael.brown@hospital.com',
    role: 'doctor',
    department: 'Pediatrics',
    specialization: 'General Pediatrics'
  },
  {
    id: 'd3',
    name: 'Dr. Emily Chen',
    email: 'emily.chen@hospital.com',
    role: 'doctor',
    department: 'Neurology',
    specialization: 'Neurological Surgery'
  },
  {
    id: 'd4',
    name: 'Dr. James Martinez',
    email: 'james.martinez@hospital.com',
    role: 'doctor',
    department: 'Orthopedics',
    specialization: 'Sports Medicine'
  },
  {
    id: 'd5',
    name: 'Dr. Lisa Thompson',
    email: 'lisa.thompson@hospital.com',
    role: 'doctor',
    department: 'Dermatology',
    specialization: 'Clinical Dermatology'
  }
];

// Mock staff data
export const mockStaff: User[] = [
  {
    id: 's1',
    name: 'John Davis',
    email: 'john.davis@hospital.com',
    role: 'patient',
    department: 'Reception'
  },
  {
    id: 's2',
    name: 'Maria Garcia',
    email: 'maria.garcia@hospital.com',
    role: 'patient',
    department: 'Nursing'
  }
];

// Mock admin data
export const mockAdmins: User[] = [
  {
    id: 'a1',
    name: 'Admin User',
    email: 'admin@hospital.com',
    role: 'admin'
  }
];

// Generate random appointments
const generateRandomAppointments = (count: number): Appointment[] => {
  const reasons = [
    'Annual Checkup',
    'Follow-up',
    'Consultation',
    'Vaccination',
    'Blood Test',
    'X-Ray',
    'Physical Therapy',
    'Dental Cleaning',
    'Eye Examination',
    'Skin Examination'
  ];

  const patients = [
    { name: 'John Smith', email: 'john.smith@email.com', phone: '(555) 123-4567' },
    { name: 'Emma Johnson', email: 'emma.j@email.com', phone: '(555) 234-5678' },
    { name: 'Michael Williams', email: 'm.williams@email.com', phone: '(555) 345-6789' },
    { name: 'Sophia Brown', email: 'sophia.b@email.com', phone: '(555) 456-7890' },
    { name: 'William Davis', email: 'w.davis@email.com', phone: '(555) 567-8901' },
    { name: 'Olivia Miller', email: 'olivia.m@email.com', phone: '(555) 678-9012' },
    { name: 'James Wilson', email: 'j.wilson@email.com', phone: '(555) 789-0123' },
    { name: 'Ava Anderson', email: 'ava.a@email.com', phone: '(555) 890-1234' },
    { name: 'Alexander Lee', email: 'alex.lee@email.com', phone: '(555) 901-2345' },
    { name: 'Isabella Taylor', email: 'i.taylor@email.com', phone: '(555) 012-3456' }
  ];

  const departments = [
    'Cardiology',
    'Pediatrics',
    'Neurology',
    'Orthopedics',
    'Dermatology'
  ];

  const appointments: Appointment[] = [];
  const today = new Date();

  for (let i = 0; i < count; i++) {
    const randomDate = new Date(today);
    randomDate.setDate(today.getDate() + Math.floor(Math.random() * 30) - 15); // -15 to +15 days
    const randomHour = 8 + Math.floor(Math.random() * 9); // 8 AM to 5 PM
    const randomMinute = Math.floor(Math.random() * 4) * 15; // 0, 15, 30, or 45 minutes

    const patient = patients[Math.floor(Math.random() * patients.length)];
    const doctor = mockDoctors[Math.floor(Math.random() * mockDoctors.length)];
    const status = Math.random() < 0.6 ? 'scheduled' : (Math.random() < 0.8 ? 'completed' : 'cancelled');

    appointments.push({
      id: `apt${i + 1}`,
      patientName: patient.name,
      patientEmail: patient.email,
      patientPhone: patient.phone,
      date: randomDate.toISOString().split('T')[0],
      time: `${randomHour.toString().padStart(2, '0')}:${randomMinute.toString().padStart(2, '0')}`,
      reason: reasons[Math.floor(Math.random() * reasons.length)],
      status: status,
      doctor: doctor.name,
      department: doctor.department || departments[Math.floor(Math.random() * departments.length)],
      notes: Math.random() > 0.7 ? 'Patient has previous medical history.' : undefined
    });
  }

  // Sort appointments by date and time
  return appointments.sort((a, b) => {
    const dateComparison = a.date.localeCompare(b.date);
    if (dateComparison !== 0) return dateComparison;
    return a.time.localeCompare(b.time);
  });
};

// Export mock appointments
export const mockAppointments = generateRandomAppointments(50);

// Mock authentication function
// eslint-disable-next-line @typescript-eslint/no-unused-vars
export const mockAuth = (email: string, password: string): User | null => {
  const allUsers = [...mockAdmins, ...mockDoctors, ...mockStaff];
  // For demo purposes, any password works, just match the email
  const user = allUsers.find(u => u.email === email);
  return user || null;
};

// Mock appointment management functions
export const mockUpdateAppointment = (
  appointmentId: string,
  updates: Partial<Appointment>
): Appointment => {
  const appointment = mockAppointments.find(a => a.id === appointmentId);
  if (!appointment) {
    throw new Error('Appointment not found');
  }
  return { ...appointment, ...updates };
};

// Get appointments by doctor
export const getAppointmentsByDoctor = (doctorId: string): Appointment[] => {
  const doctor = mockDoctors.find(d => d.id === doctorId);
  if (!doctor) return [];
  return mockAppointments.filter(apt => apt.doctor === doctor.name);
};

// Get appointments by department
export const getAppointmentsByDepartment = (department: string): Appointment[] => {
  return mockAppointments.filter(apt => apt.department === department);
};

// Get appointments by date range
export const getAppointmentsByDateRange = (startDate: string, endDate: string): Appointment[] => {
  return mockAppointments.filter(apt => 
    apt.date >= startDate && apt.date <= endDate
  );
}; 