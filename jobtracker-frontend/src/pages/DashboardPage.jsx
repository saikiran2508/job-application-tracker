import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getApplications, deleteApplication, updateApplicationStatus } from '../api/applicationApi';

function DashboardPage() {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchApplications();
  }, []);

  const fetchApplications = async () => {
    try {
      const response = await getApplications();
      setApplications(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Could not load applications.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteApplication(id);
      setApplications(applications.filter((app) => app.id !== id));
    } catch (err) {
      setError(err.response?.data?.message || 'Could not delete application.');
    }
  };

  const handleStatusChange = async (id, newStatus) => {
  try {
    const response = await updateApplicationStatus(id, newStatus);
    setApplications(
      applications.map((app) => (app.id === id ? response.data : app))
    );
  } catch (err) {
    setError(err.response?.data?.message || 'Could not update status.');
  }
};

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div className="dashboard-container">
    <div className="header-row">
      <h2>My Applications</h2>
      <div className="button-group">
        <button onClick={() => navigate('/applications/new')}>Add Application</button>
        <button className="btn-secondary" onClick={handleLogout}>Logout</button>
      </div>
    </div>
    {error && <p className="error-text">{error}</p>}
      {applications.length === 0 ? (
        <p>No applications yet.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Company</th>
              <th>Role</th>
              <th>Status</th>
              <th>Date Applied</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {applications.map((app) => (
              <tr key={app.id}>
                <td>{app.companyName}</td>
                <td>{app.roleTitle}</td>
                <td>
                  <select
                    value={app.status}
                    onChange={(e) => handleStatusChange(app.id, e.target.value)}
                  >
                    <option value="APPLIED">Applied</option>
                    <option value="INTERVIEW">Interview</option>
                    <option value="OFFER">Offer</option>
                    <option value="REJECTED">Rejected</option>
                  </select>
              </td>
                <td>{app.dateApplied}</td>
                <td>
                  <button className="btn-danger" onClick={() => handleDelete(app.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default DashboardPage;