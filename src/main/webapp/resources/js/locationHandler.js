function addLocation(button) {
    const data = {
        name: button.dataset.name,
        latitude: button.dataset.latitude,
        longitude: button.dataset.longitude,
        country: button.dataset.country,
        state: button.dataset.state
    };

    fetch('api/locations', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.status === 401) {
                window.location.href = '/login';
                return;
            }
            if (!response.ok) {
                throw new Error();
            }
            button.style.display = 'none';
            const badge = document.createElement('span');
            badge.className = 'badge bg-warning bg-opacity-25 text-warning-emphasis px-3 py-2 d-block w-100';
            badge.style.fontSize = '0.875rem';
            badge.innerHTML = '<i class="fas fa-star me-1"></i>Added';
            const actionContainer = button.closest('.action-container');
            actionContainer.appendChild(badge);
            console.log('Location added successfully');
        })
        .catch(error => {
            console.error('Error adding location:', data.name);
            showErrorNotification('Unable to save location. Please try again in a few minutes.');
        });
}

function removeLocation(button) {
    const locationId = button.getAttribute('data-id');

    fetch(`/api/locations/${locationId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.status === 401) {
                window.location.href = '/login';
                return;
            }
            if (!response.ok) {
                throw new Error();
            }
            const card = button.closest('.location-card');
            if (card) {
                card.remove();
            }
            console.log('Location removed successfully');
        })
        .catch(error => {
            console.error('Error remove location:', locationId);
            showErrorNotification("Unable to remove location. Please try again in a few minutes.");
        });
}

function showErrorNotification(message) {
    const notification = document.createElement('div');
    notification.className = 'alert alert-danger position-fixed top-0 end-0 m-3';
    notification.style.zIndex = '1050';
    notification.innerHTML = `<i class="fas fa-exclamation-circle me-2"></i>${message}`;
    document.body.appendChild(notification);
    setTimeout(() => {
        notification.remove();
    }, 3000);
}