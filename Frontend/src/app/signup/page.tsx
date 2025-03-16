'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/context/AuthContext';

export default function SignupPage() {
  const router = useRouter();
  const { signup } = useAuth();
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    username: '',
    password: '',
    confirmPassword: '',
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      setIsLoading(false);
      return;
    }

    try {
      await signup({
        name: formData.name,
        email: formData.email,
        username: formData.username,
        password: formData.password,
      });
      setShowModal(true);
      // Redirect to login page after 3 seconds
      setTimeout(() => {
        router.push('/login');
      }, 3000);
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Registration failed');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
          <div className="fixed inset-0 bg-black opacity-50"></div>
          <div className="relative z-50 bg-white rounded-lg p-8 max-w-md mx-4 text-center">
            <div className="mb-6">
              <svg
                className="mx-auto h-12 w-12 text-green-500"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
            </div>
            <h2 className="text-2xl font-bold text-[#1e4d90] mb-4">
              Account Created Successfully!
            </h2>
            <p className="text-gray-600 mb-6">
              Please check your email to verify your account. You will be redirected to the login page shortly.
            </p>
            <button
              onClick={() => router.push('/login')}
              className="bg-[#1e4d90] text-white px-6 py-2 rounded-md hover:bg-[#163c72] transition-colors"
            >
              Go to Login
            </button>
          </div>
        </div>
      )}

      {/* Main Content */}
      <div className="min-h-screen flex items-center justify-center bg-[#f8fafc] px-4 py-12">
        <div className="max-w-md w-full">
          <div className="text-center mb-8">
            <h1 className="text-3xl font-bold text-[#1e4d90] mb-2">Create Account</h1>
            <p className="text-gray-600">Join our healthcare platform</p>
          </div>

          <div className="bg-white p-8 rounded-lg shadow-sm">
            {error && (
              <div className="mb-4 p-3 bg-red-50 text-red-500 rounded-md text-sm">
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Full Name
                </label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-[#1e4d90] focus:border-transparent text-gray-900 placeholder-gray-500"
                  placeholder="John Doe"
                  required
                  disabled={isLoading}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Email Address
                </label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-[#1e4d90] focus:border-transparent text-gray-900 placeholder-gray-500"
                  placeholder="john@example.com"
                  required
                  disabled={isLoading}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Username
                </label>
                <input
                  type="text"
                  value={formData.username}
                  onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-[#1e4d90] focus:border-transparent text-gray-900 placeholder-gray-500"
                  placeholder="johndoe123"
                  required
                  disabled={isLoading}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Password
                </label>
                <input
                  type="password"
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-[#1e4d90] focus:border-transparent text-gray-900 placeholder-gray-500"
                  placeholder="••••••••"
                  required
                  disabled={isLoading}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Confirm Password
                </label>
                <input
                  type="password"
                  value={formData.confirmPassword}
                  onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-[#1e4d90] focus:border-transparent text-gray-900 placeholder-gray-500"
                  placeholder="••••••••"
                  required
                  disabled={isLoading}
                />
              </div>

              <div className="space-y-4">
                <button
                  type="submit"
                  className="w-full bg-[#1e4d90] text-white py-2 px-4 rounded-md hover:bg-[#163c72] transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                  disabled={isLoading}
                >
                  {isLoading ? 'Creating Account...' : 'Create Account'}
                </button>
              </div>
            </form>

            <div className="mt-6 text-center text-sm">
              <span className="text-gray-600">Already have an account?</span>{' '}
              <Link href="/login" className="text-[#1e4d90] hover:text-[#163c72] font-medium">
                Sign in
              </Link>
            </div>
          </div>
        </div>
      </div>
    </>
  );
} 