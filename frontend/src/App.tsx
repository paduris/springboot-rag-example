import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import { Box, CssBaseline, ThemeProvider, createTheme, Toolbar } from '@mui/material';
import { Toaster } from 'react-hot-toast';

import Header from './components/Header';
import Sidebar from './components/Sidebar';
import Footer from './components/Footer';
import FileUpload from './components/FileUpload';
import Chat from './components/Chat';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

const AppContent = () => {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const navigate = useNavigate();
  const [documentUploaded, setDocumentUploaded] = useState(false);

  const handleMenuClick = () => {
    setSidebarOpen(!sidebarOpen);
  };

  const handleNavigation = (path: string) => {
    navigate(path);
  };

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', flexDirection: 'column' }}>
      <CssBaseline />
      <Header onMenuClick={handleMenuClick} />
      <Sidebar 
        open={sidebarOpen} 
        onClose={() => setSidebarOpen(false)}
        onNavigate={handleNavigation}
      />
      
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 3,
          width: { sm: `calc(100% - 240px)` },
          ml: '240px'
        }}
      >
        <Toolbar />
        <Routes>
          <Route path="/" element={<FileUpload onUploadSuccess={() => setDocumentUploaded(true)} />} />
          <Route path="/upload" element={<FileUpload onUploadSuccess={() => setDocumentUploaded(true)} />} />
          <Route path="/chat" element={documentUploaded ? <Chat /> : <FileUpload onUploadSuccess={() => setDocumentUploaded(true)} />} />
        </Routes>
        <Footer />
      </Box>

      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3000,
          style: {
            background: '#363636',
            color: '#fff',
          },
        }}
      />
    </Box>
  );
};

const App = () => {
  return (
    <ThemeProvider theme={darkTheme}>
      <Router>
        <AppContent />
      </Router>
    </ThemeProvider>
  );
};

export default App;
