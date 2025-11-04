/** @type {import('tailwindcss').Config} */
export default {
  // 1. Aquí le decimos a Tailwind dónde buscar sus clases
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}", // Busca en todos tus archivos de React
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}