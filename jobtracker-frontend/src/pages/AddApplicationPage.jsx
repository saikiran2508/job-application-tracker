import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createApplication } from '../api/applicationApi';

function AddApplicationPage() {
  const [formData, setFormData] = useState({
    companyName: '',
    roleTitle: '',
    jobLink: '',
    status: 'APPLIED',
    dateApplied: '',
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      await createApplication(formData);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create application.');
    }
  };

  return (
    <div className="page-container">
      <h2>Add Application</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="companyName"
          placeholder="Company Name"
          value={formData.companyName}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="roleTitle"
          placeholder="Role Title"
          value={formData.roleTitle}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="jobLink"
          placeholder="Job Link (optional)"
          value={formData.jobLink}
          onChange={handleChange}
        />
        <select name="status" value={formData.status} onChange={handleChange}>
          <option value="APPLIED">Applied</option>
          <option value="INTERVIEW">Interview</option>
          <option value="OFFER">Offer</option>
          <option value="REJECTED">Rejected</option>
        </select>
        <input
          type="date"
          name="dateApplied"
          value={formData.dateApplied}
          onChange={handleChange}
          required
        />
        <button type="submit">Add Application</button>
      </form>
      {error && <p className="error-text">{error}</p>}
    </div>
  );
}

export default AddApplicationPage;