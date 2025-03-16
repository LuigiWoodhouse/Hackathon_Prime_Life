import type { Metadata } from 'next';
import { Poppins } from 'next/font/google';
import { AuthProvider } from '@/context/AuthContext';
import Navbar from '@/components/Navbar';
import './globals.css';

const poppins = Poppins({ 
  weight: ['400', '500', '600', '700'],
  subsets: ['latin']
});

export const metadata: Metadata = {
  title: "Doctor's Appointment Planner",
  description: 'Schedule and manage doctor appointments efficiently',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className={`${poppins.className} bg-[#f8fafc]`}>
        <AuthProvider>
          <Navbar />
          <main>
            {children}
          </main>
        </AuthProvider>
      </body>
    </html>
  );
} 