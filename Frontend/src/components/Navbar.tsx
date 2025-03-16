'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useAuth } from '@/context/AuthContext';
import { usePathname } from 'next/navigation';

export default function Navbar() {
  const { user, logout } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const pathname = usePathname();

  return (
    <nav className="bg-[#1e4d90] text-white">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <div className="flex-shrink-0">
            <Link href="/" className="text-xl font-bold">
              PrimeLife
            </Link>
          </div>

          {/* Mobile menu button */}
          <div className="md:hidden">
            <button
              onClick={() => setIsMenuOpen(!isMenuOpen)}
              className="inline-flex items-center justify-center p-2 rounded-md text-white hover:bg-[#1e3a6a] focus:outline-none"
            >
              <svg
                className="h-6 w-6"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                {isMenuOpen ? (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                ) : (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                )}
              </svg>
            </button>
          </div>

          {/* Desktop navigation */}
          {user && (
            <div className="hidden md:flex space-x-6">
              {user.role === 'admin' && (
                <Link 
                  href="/admin" 
                  className={`text-blue-100 hover:text-white transition-colors ${
                    pathname === '/admin' ? 'text-white font-medium' : ''
                  }`}
                >
                  Admin Panel
                </Link>
              )}
              {user.role !== 'admin' && (
                <>
                  {/* <Link 
                    href="/" 
                    className={`text-blue-100 hover:text-white transition-colors ${
                      pathname === '/' ? 'text-white font-medium' : ''
                    }`}
                  >
                    Dashboard
                  </Link>
                  <Link 
                    href="/appointments" 
                    className={`text-blue-100 hover:text-white transition-colors ${
                      pathname === '/appointments' ? 'text-white font-medium' : ''
                    }`}
                  >
                    Appointments
                  </Link> */}
                </>
              )}
            </div>
          )}

          {/* User menu - Desktop */}
          <div className="hidden md:flex items-center space-x-4">
            {user ? (
              <>
                <div className="text-right">
                  <div className="text-sm font-medium">{user.name}</div>
                  <div className="text-xs text-blue-100">
                    {user.role.charAt(0).toUpperCase() + user.role.slice(1)}
                  </div>
                </div>
                <button
                  onClick={logout}
                  className="bg-white text-[#1e4d90] px-4 py-2 rounded-lg hover:bg-blue-50 transition-colors text-sm font-medium"
                >
                  Sign Out
                </button>
              </>
            ) : (
              <div className="space-x-4">
                {/* <Link
                  href="/login"
                  className="text-blue-100 hover:text-white transition-colors"
                >
                  Sign In
                </Link>
                <Link
                  href="/signup"
                  className="bg-white text-[#1e4d90] px-4 py-2 rounded-lg hover:bg-blue-50 transition-colors text-sm font-medium"
                >
                  Sign Up
                </Link> */}
              </div>
            )}
          </div>
        </div>

        {/* Mobile menu */}
        <div className={`${isMenuOpen ? 'block' : 'hidden'} md:hidden pb-4`}>
          {user ? (
            <>
              <div className="pt-2 pb-3 space-y-1">
                {user.role === 'admin' ? (
                  <Link
                    href="/admin"
                    className={`block px-3 py-2 rounded-md text-base font-medium ${
                      pathname === '/admin'
                        ? 'bg-[#1e3a6a] text-white'
                        : 'text-blue-100 hover:text-white hover:bg-[#1e3a6a]'
                    }`}
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Admin Panel
                  </Link>
                ) : (
                  <>
                    {/* <Link
                      href="/"
                      className={`block px-3 py-2 rounded-md text-base font-medium ${
                        pathname === '/'
                          ? 'bg-[#1e3a6a] text-white'
                          : 'text-blue-100 hover:text-white hover:bg-[#1e3a6a]'
                      }`}
                      onClick={() => setIsMenuOpen(false)}
                    >
                      Dashboard
                    </Link>
                    <Link
                      href="/appointments"
                      className={`block px-3 py-2 rounded-md text-base font-medium ${
                        pathname === '/appointments'
                          ? 'bg-[#1e3a6a] text-white'
                          : 'text-blue-100 hover:text-white hover:bg-[#1e3a6a]'
                      }`}
                      onClick={() => setIsMenuOpen(false)}
                    >
                      Appointments
                    </Link> */}
                  </>
                )}
              </div>
              <div className="pt-4 pb-3 border-t border-[#1e3a6a]">
                <div className="px-3">
                  <div className="text-base font-medium">{user.name}</div>
                  <div className="text-sm text-blue-100">
                    {user.role.charAt(0).toUpperCase() + user.role.slice(1)}
                  </div>
                </div>
                <div className="mt-3 px-3">
                  <button
                    onClick={() => {
                      logout();
                      setIsMenuOpen(false);
                    }}
                    className="w-full bg-white text-[#1e4d90] px-4 py-2 rounded-lg hover:bg-blue-50 transition-colors text-sm font-medium"
                  >
                    Sign Out
                  </button>
                </div>
              </div>
            </>
          ) : (
            <div className="pt-2 pb-3 space-y-2 px-3">
              {/* <Link
                href="/login"
                className="block text-blue-100 hover:text-white hover:bg-[#1e3a6a] px-3 py-2 rounded-md text-base font-medium"
                onClick={() => setIsMenuOpen(false)}
              >
                Sign In
              </Link>
              <Link
                href="/signup"
                className="block bg-white text-[#1e4d90] px-3 py-2 rounded-md text-base font-medium hover:bg-blue-50"
                onClick={() => setIsMenuOpen(false)}
              >
                Sign Up
              </Link> */}
            </div>
          )}
        </div>
      </div>
    </nav>
  );
} 