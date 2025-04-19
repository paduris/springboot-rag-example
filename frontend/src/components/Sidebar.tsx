import { Drawer, List, ListItemButton, ListItemIcon, ListItemText, Box, Toolbar } from '@mui/material';
import { Upload, Chat as ChatIcon } from '@mui/icons-material';

interface SidebarProps {
  open: boolean;
  onClose: () => void;
  onNavigate: (path: string) => void;
}

const drawerWidth = 240;

const Sidebar = ({ open, onClose, onNavigate }: SidebarProps) => {
  const menuItems = [
    { text: 'Upload Documents', icon: <Upload />, path: '/upload' },
    { text: 'Chat', icon: <ChatIcon />, path: '/chat' },
  ];

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: drawerWidth,
          boxSizing: 'border-box',
        },
      }}
    >
      <Toolbar />
      <Box sx={{ overflow: 'auto' }}>
        <List>
          {menuItems.map((item) => (
            <ListItemButton 
              key={item.text}
              onClick={() => onNavigate(item.path)}
            >
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          ))}
        </List>
      </Box>
    </Drawer>
  );
};

export default Sidebar;
