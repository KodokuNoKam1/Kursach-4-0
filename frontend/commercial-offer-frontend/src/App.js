import AuditLogs from './components/AuditLogs';
import Categories from './components/Categories';

const App = () => {
    const handleLogout = () => {
        localStorage.removeItem('token');
        window.location.href = '/login';
    };

    return (
        <Router>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" style={{ flexGrow: 1 }}>
                        Commercial Offer System
                    </Typography>
                    <Button color="inherit" component={Link} to="/offers">
                        Offers
                    </Button>
                    <Button color="inherit" component={Link} to="/users">
                        Users
                    </Button>
                    <Button color="inherit" component={Link} to="/notifications">
                        Notifications
                    </Button>
                    <Button color="inherit" component={Link} to="/statistics">
                        Statistics
                    </Button>
                    <Button color="inherit" component={Link} to="/audit">
                        Audit Logs
                    </Button>
                    <Button color="inherit" component={Link} to="/categories">
                        Categories
                    </Button>
                    <Button color="inherit" onClick={handleLogout}>
                        Logout
                    </Button>
                </Toolbar>
            </AppBar>
            <Container style={{ marginTop: '20px' }}>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/offers" element={<Offers />} />
                    <Route path="/users" element={<Users />} />
                    <Route path="/notifications" element={<Notifications />} />
                    <Route path="/statistics" element={<Statistics />} />
                    <Route path="/audit" element={<AuditLogs />} />
                    <Route path="/categories" element={<Categories />} />
                    <Route path="/" element={<Login />} />
                </Routes>
            </Container>
        </Router>
    );
};