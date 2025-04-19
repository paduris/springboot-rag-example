import { useState } from 'react';
import { Box, Button, Paper, Typography, CircularProgress } from '@mui/material';
import { CloudUpload } from '@mui/icons-material';
import axios from 'axios';
import toast from 'react-hot-toast';

interface FileUploadProps {
  onUploadSuccess: () => void;
}

const FileUpload = ({ onUploadSuccess }: FileUploadProps) => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files.length > 0) {
      setSelectedFile(event.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      toast.error('Please select a file first');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);
    setUploading(true);

    try {
      await axios.post('http://localhost:8081/api/rag/documents', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      toast.success('File uploaded successfully');
      onUploadSuccess();
      setSelectedFile(null);
    } catch (error) {
      console.error('Error uploading file:', error);
      toast.error('Error uploading file');
    } finally {
      setUploading(false);
    }
  };

  return (
    <Paper elevation={3} sx={{ p: 4, textAlign: 'center' }}>
      <Typography variant="h5" gutterBottom>
        Upload Documents
      </Typography>
      <Box sx={{ my: 3 }}>
        <input
          accept=".pdf,.doc,.docx,.txt"
          style={{ display: 'none' }}
          id="file-upload"
          type="file"
          onChange={handleFileChange}
        />
        <label htmlFor="file-upload">
          <Button
            variant="outlined"
            component="span"
            startIcon={<CloudUpload />}
            sx={{ mb: 2 }}
          >
            Choose File
          </Button>
        </label>
        {selectedFile && (
          <Typography variant="body2" color="textSecondary" sx={{ mb: 2 }}>
            Selected: {selectedFile.name}
          </Typography>
        )}
        <Box>
          <Button
            variant="contained"
            onClick={handleUpload}
            disabled={!selectedFile || uploading}
            sx={{ minWidth: 120 }}
          >
            {uploading ? <CircularProgress size={24} /> : 'Upload'}
          </Button>
        </Box>
      </Box>
    </Paper>
  );
}

export default FileUpload;
