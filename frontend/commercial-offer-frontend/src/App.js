import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Container, createTheme, ThemeProvider } from '@mui/material';
import Login from './components/Login';
import Offers from './components/Offers';
import Users from './components/Users';
import Notifications from './components/Notifications';
import Statistics from './components/Statistics';
import AuditLogs from './components/AuditLogs';
import Categories from './components/Categories';
import PrivateRoute from './components/PrivateRoute';
import OfferIcon from '@mui/icons-material/RequestQuote';
import PeopleIcon from '@mui/icons-material/People';
import NotificationsIcon from '@mui/icons-material/Notifications';
import BarChartIcon from '@mui/icons-material/BarChart';
import HistoryIcon from '@mui/icons-material/History';
import CategoryIcon from '@mui/icons-material/Category';
import LogoutIcon from '@mui/icons-material/Logout';

const theme = createTheme({
    breakpoints: {
        values: {
            xs: 0,
            sm: 600,
            md: 960,
            lg: 1280,
            xl: 1920,
        },
    },
});

const App = () => {
    const handleLogout = () => {
        localStorage.removeItem('token');
        window.location.href = '/login';
    };

    return (
        <ThemeProvider theme={theme}>
            <Router>
                <AppBar position="static">
                    <Toolbar>
                        <Typography variant="h6" style={{ flexGrow: 1 }}>
                            Commercial Offer System
                        </Typography>
                        <Button color="inherit" component={Link} to="/offers" startIcon={<OfferIcon />}>
                            Offers
                        </Button>
                        <Button color="inherit" component={Link} to="/users" startIcon={<PeopleIcon />}>
                            Users
                        </Button>
                        <Button color="inherit" component={Link} to="/notifications" startIcon={<NotificationsIcon />}>
                            Notifications
                        </Button>
                        <Button color="inherit" component={Link} to="/statistics" startIcon={<BarChartIcon />}>
                            Statistics
                        </Button>
                        <Button color="inherit" component={Link} to="/audit" startIcon={<HistoryIcon />}>
                            Audit Logs
                        </Button>
                        <Button color="inherit" component={Link} to="/categories" startIcon={<CategoryIcon />}>
                            Categories
                        </Button>
                        <Button color="inherit" onClick={handleLogout} startIcon={<LogoutIcon />}>
                            Logout
                        </Button>
                    </Toolbar>
                </AppBar>
                <Container style={{ marginTop: '20px' }}>
                    <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route
                            path="/offers"
                            element={
                                <PrivateRoute>
                                    <Offers />
                                </PrivateRoute>
                            }
                        />
                        <Route
                            path="/users"
                            element={
                                <PrivateRoute>
                                    <Users />
                                </PrivateRoute>
                            }
                        />
                        <Route
                            path="/notifications"
                            element={
                                <PrivateRoute>
                                    <Notifications />
                                </PrivateRoute>
                            }
                        />
                        <Route
                            path="/statistics"
                            element={
                                <PrivateRoute>
                                    <Statistics />
                                </PrivateRoute>
                            }
                        />
                        <Route
                            path="/audit"
                            element={
                                <PrivateRoute>
                                    <AuditLogs />
                                </PrivateRoute>
                            }
                        />
                        <Route
                            path="/categories"
                            element={
                                <PrivateRoute>
                                    <Categories />
                                </PrivateRoute>
                            }
                        />
                        <Route path="/" element={<Login />} />
                    </Routes>
                </Container>
            </Router>
        </ThemeProvider>
    );
};

export default App;